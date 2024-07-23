package com.aivle.bit.company.service;

import com.aivle.bit.company.domain.Company;
import com.aivle.bit.company.repository.CompanyRepository;
import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.global.exception.ErrorCode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private static final long MAX_IMAGE_SIZE = 1_048_576; // 1MB

    @Transactional
    public Company addCompany(String name, String businessType, MultipartFile image) {
        companyRepository.findByName(name).ifPresent(c -> {
            throw new AivleException(ErrorCode.ALREADY_REGISTERED_COMPANY);
        });

        String imageUrl = saveImage(image);
        Company company = Company.of(name, businessType, imageUrl);
        return companyRepository.save(company);
    }

    @Transactional
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
            .orElseThrow(() -> new AivleException(ErrorCode.NO_COMPANY_WITH_ID));
        company.delete();
        companyRepository.save(company);
    }

    private String saveImage(MultipartFile image) {

        if (image.getSize() > MAX_IMAGE_SIZE) {
            throw new AivleException(ErrorCode.IMAGE_SIZE_EXCEEDED);
        }

        try {
            byte[] bytes = image.getBytes();
            Path path = Paths.get("images/" + image.getOriginalFilename());
            Files.createDirectories(path.getParent());
            Files.write(path, bytes);
            return path.toString();
        } catch (IOException e) {
            throw new AivleException(ErrorCode.SAVE_IMG_ERROR);
        }
    }
}