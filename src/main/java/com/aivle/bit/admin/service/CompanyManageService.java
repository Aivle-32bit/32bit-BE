package com.aivle.bit.admin.service;

import static com.aivle.bit.global.exception.ErrorCode.NOT_CSV_FILE;
import static com.aivle.bit.global.exception.ErrorCode.NO_SEARCH_COMPANY;
import static com.aivle.bit.global.exception.ErrorCode.OVER_MAX_FILE_SIZE;
import static com.aivle.bit.global.exception.ErrorCode.SERVER_ERROR;

import com.aivle.bit.company.domain.Company;
import com.aivle.bit.company.domain.FinancialSummary;
import com.aivle.bit.company.domain.Metrics;
import com.aivle.bit.company.domain.MetricsSummary;
import com.aivle.bit.company.domain.Swot;
import com.aivle.bit.company.domain.SwotCategory;
import com.aivle.bit.company.repository.CompanyRepository;
import com.aivle.bit.company.repository.FinancialSummaryRepository;
import com.aivle.bit.company.repository.MetricsSummaryRepository;
import com.aivle.bit.company.repository.SwotRepository;
import com.aivle.bit.company.service.S3Service;
import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.global.exception.ErrorCode;
import com.aivle.bit.member.domain.Member;
import com.aivle.bit.member.repository.MemberRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CompanyManageService {

    private static final int MAX_FILE_SIZE = 1024 * 1024 * 5;
    private static final List<String> REQUIRED_COLUMNS = Arrays.asList("부채총계(연결)", "자산총계(연결)", "자본총계(연결)",
        "전년도자산총계(연결)", "영업활동으로인한현금흐름(연결)",
        "총당기순이익(연결)", "유형자산(계)(연결)", "매출채권(연결)", "매출액(연결)", "영업이익(손실)(연결)", "매출총이익(손실)",
        "매출원가(손실)(연결)",
        "유동부채");

    @Value("${api.server}")
    private String API_SERVER;

    private final S3Service s3Service;
    private final CompanyRepository companyRepository;
    private final MemberRepository memberRepository;
    private final FinancialSummaryRepository financialSummaryRepository;
    private final MetricsSummaryRepository metricsSummaryRepository;
    private final SwotRepository swotRepository;
    private final WebClient webClient;

    public PagedModel<Company> getAllCompanies(Pageable pageable) {
        return new PagedModel<>(companyRepository.findAllByIsDeletedFalse(pageable));
    }

    @Transactional
    public void deleteCompany(Long id) {
        Company company = companyRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
            () -> new AivleException(NO_SEARCH_COMPANY)
        );
        company.delete();

        List<Member> members = memberRepository.findByCompanyId(id);
        if (members != null && !members.isEmpty()) {
            for (Member member : members) {
                member.updateCompany(null);
                memberRepository.save(member);
            }
        }
        companyRepository.save(company);
    }

    @Transactional
    public void createReports(Long id, MultipartFile file) {
        Company company = companyRepository.findById(id).orElseThrow(
            () -> new AivleException(NO_SEARCH_COMPANY)
        );
        validateFile(file);
        Map<String, Object> data = parseCsvFile(file, company);
        String response = sendToFlaskAPI(data);
        saveOrUpdateReports(response, company);
    }

    private void validateFile(MultipartFile file) {
        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".csv")) {
            throw new AivleException(NOT_CSV_FILE);
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new AivleException(OVER_MAX_FILE_SIZE);
        }
    }

    private Map<String, Object> parseCsvFile(MultipartFile file, Company company) {
        Map<String, Object> data = new HashMap<>();
        try (CSVReader reader = new CSVReaderBuilder(
            new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
            .withSkipLines(1) // 헤더 라인을 스킵
            .build()) {

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                for (int i = 0; i < REQUIRED_COLUMNS.size(); i++) {
                    String column = REQUIRED_COLUMNS.get(i);
                    data.put(column, Double.parseDouble(nextLine[i]));
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new AivleException(SERVER_ERROR);
        }
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("industry", company.getBusinessType());
        requestData.put("data", List.of(data));
        return requestData;
    }

    private String sendToFlaskAPI(Map<String, Object> data) {
        String jsonData = new Gson().toJson(data);
        jsonData = convertScientificNotation(jsonData);
        return webClient.post()
            .uri(API_SERVER)
            .header("Content-Type", "application/json")
            .body(Mono.just(jsonData), String.class)
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(throwable -> {
                throw new AivleException(SERVER_ERROR);
            })
            .block();
    }

    private void saveOrUpdateReports(String response, Company company) {
        Type listType = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> responseData = new Gson().fromJson(response, listType);
        for (Map<String, Object> record : responseData) {
            int year = LocalDate.now().getYear();
            System.out.println(record);
            saveOrUpdateFinancialSummary(year - 1, company, record, "2023");
            saveOrUpdateFinancialSummary(year, company, record, "2024");

            saveOrUpdateMetricsSummary(Metrics.DEBT, (String) record.get("DEBT"), company, year);
            saveOrUpdateMetricsSummary(Metrics.ROA, (String) record.get("ROA"), company, year);
            saveOrUpdateMetricsSummary(Metrics.ATR, (String) record.get("ATR"), company, year);
            saveOrUpdateMetricsSummary(Metrics.AGR, (String) record.get("AGR"), company, year);
            saveOrUpdateMetricsSummary(Metrics.PPE, (String) record.get("PPE"), company, year);

            Map<String, List<String>> swotSections = new Gson().fromJson((String) record.get("SWOT 분석"), Map.class);
            saveOrUpdateSwot(year, company, SwotCategory.STRENGTH, swotSections.get("strengths"));
            saveOrUpdateSwot(year, company, SwotCategory.WEAKNESS, swotSections.get("weaknesses"));
            saveOrUpdateSwot(year, company, SwotCategory.OPPORTUNITY, swotSections.get("opportunities"));
            saveOrUpdateSwot(year, company, SwotCategory.THREAT, swotSections.get("threats"));
        }
    }

    private void saveOrUpdateFinancialSummary(int year, Company company, Map<String, Object> record, String prefix) {
        Double debt = (Double) record.get(prefix + "_DEBT");
        Double atr = (Double) record.get(prefix + "_ATR");
        Double agr = (Double) record.get(prefix + "_AGR");
        Double roa = (Double) record.get(prefix + "_ROA");
        Double ppe = (Double) record.get(prefix + "_PPE");
        Integer salesAmount = Optional.ofNullable((Double) record.get(prefix + "_매출액")).map(Double::intValue)
            .orElse(null);
        Integer netIncome = Optional.ofNullable((Double) record.get(prefix + "_당기순이익")).map(Double::intValue)
            .orElse(null);
        Integer totalLiabilities = Optional.ofNullable((Double) record.get(prefix + "_부채총계")).map(Double::intValue)
            .orElse(0);
        Integer totalAssets = Optional.ofNullable((Double) record.get(prefix + "_자산총계")).map(Double::intValue)
            .orElse(0);
        Integer operatingIncome = Optional.ofNullable((Double) record.get(prefix + "_영업이익")).map(Double::intValue)
            .orElse(null);
        Integer capitalStock = Optional.ofNullable((Double) record.get(prefix + "_자본총계")).map(Double::intValue)
            .orElse(null);
        Integer cashFlowFromOperatingActivities = Optional.ofNullable((Double) record.get(prefix + "_영업활동으로인한현금흐름"))
            .map(Double::intValue).orElse(null);
        Double roe = (Double) record.get(prefix + "_ROE");

        FinancialSummary financialSummary = financialSummaryRepository.findByCompanyAndYear(company, year)
            .orElse(FinancialSummary.of(
                year,
                company,
                debt,
                atr,
                agr,
                roa,
                ppe,
                salesAmount,
                netIncome,
                totalLiabilities,
                totalAssets,
                operatingIncome,
                capitalStock,
                cashFlowFromOperatingActivities,
                roe
            ));

        financialSummary.UpdateData(
            debt,
            atr,
            agr,
            roa,
            ppe,
            salesAmount,
            netIncome,
            totalLiabilities,
            totalAssets,
            operatingIncome,
            capitalStock,
            cashFlowFromOperatingActivities,
            roe
        );

        financialSummaryRepository.save(financialSummary);
    }

    private void saveOrUpdateMetricsSummary(Metrics metric, String value, Company company, int year) {
        MetricsSummary metricsSummary = metricsSummaryRepository.findByCompanyAndYearAndMetrics(company, year, metric)
            .orElse(MetricsSummary.of(metric, value, company, year));
        metricsSummary.updateValue(value);
        metricsSummaryRepository.save(metricsSummary);
    }


    private void saveOrUpdateSwot(int year, Company company, SwotCategory category, List<String> descriptions) {
        for (String description : descriptions) {
            Swot swot = swotRepository.findByCompanyAndYearAndCategoryAndDescription(company, year, category,
                    description)
                .orElse(Swot.of(year, company, category, description));
            swotRepository.save(swot);
        }
    }

    @Transactional
    public Company addCompany(String name, String businessType, MultipartFile image) {
        companyRepository.findByNameAndIsDeletedFalse(name).ifPresent(c -> {
            throw new AivleException(ErrorCode.ALREADY_REGISTERED_COMPANY);
        });

        String imageUrl = s3Service.uploadImage(image,"/company/image");
        Company company = Company.of(name, businessType, imageUrl);
        return companyRepository.save(company);
    }


    public static String convertScientificNotation(String input) {
        Pattern pattern = Pattern.compile("([0-9\\.]+)E(\\d+)");
        Matcher matcher = pattern.matcher(input);

        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String number = matcher.group(1);
            int exponent = Integer.parseInt(matcher.group(2));
            String replacement = String.format("%." + exponent + "f",
                Double.parseDouble(number) * Math.pow(10, exponent));
            matcher.appendReplacement(sb,
                replacement.replaceAll("\\.0+$", ""));  // Removes trailing zeros after the decimal point
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

}
