package com.aivle.bit.company.service;

import com.aivle.bit.company.dto.response.SwotResponse;
import com.aivle.bit.company.dto.response.SwotResponse.SwotItem;
import com.aivle.bit.company.repository.SwotRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SwotService {

    private final SwotRepository swotRepository;

    public SwotResponse getSwotReport(Long companyId) {
        int currentYear = LocalDate.now().getYear();
        List<SwotItem> swotItems = swotRepository.findByCompanyIdAndYear(companyId, currentYear).stream()
            .map(item -> SwotItem.of(item.getCategory().name(),
                item.getDescription())).toList();

        List<SwotItem> strengths = filterSwotItems(swotItems, "STRENGTH");
        List<SwotItem> weaknesses = filterSwotItems(swotItems, "WEAKNESS");
        List<SwotItem> opportunities = filterSwotItems(swotItems, "OPPORTUNITY");
        List<SwotItem> threats = filterSwotItems(swotItems, "THREAT");

        return SwotResponse.of(strengths, weaknesses, opportunities, threats);
    }

    private List<SwotItem> filterSwotItems(List<SwotItem> items, String type) {
        return items.stream()
            .filter(item -> item.getType().equalsIgnoreCase(type))
            .collect(Collectors.toList());
    }
}
