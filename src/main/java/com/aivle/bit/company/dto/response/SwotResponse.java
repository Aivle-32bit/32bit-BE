package com.aivle.bit.company.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class SwotResponse {

    private final List<SwotItem> strengths;
    private final List<SwotItem> weaknesses;
    private final List<SwotItem> opportunities;
    private final List<SwotItem> threats;

    private SwotResponse(List<SwotItem> strengths, List<SwotItem> weaknesses,
                         List<SwotItem> opportunities, List<SwotItem> threats) {
        this.strengths = strengths;
        this.weaknesses = weaknesses;
        this.opportunities = opportunities;
        this.threats = threats;
    }

    public static SwotResponse of(List<SwotItem> strengths, List<SwotItem> weaknesses,
                                  List<SwotItem> opportunities, List<SwotItem> threats) {
        return new SwotResponse(strengths, weaknesses, opportunities, threats);
    }

    @Getter
    public static class SwotItem {

        private final String type;
        private final String description;

        private SwotItem(String type, String description) {
            this.type = type;
            this.description = description;
        }

        public static SwotItem of(String type, String description) {
            return new SwotItem(type, description);
        }
    }
}
