package com.aivle.bit.company.dto.response;

import com.aivle.bit.company.domain.CompanyRegistration;
import com.aivle.bit.company.domain.VerificationStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Function;
import lombok.Getter;

@Getter
public class CompanyRegistrationResponse {

    private final UUID id;
    private final String name;
    private final String address;
    private final String representativeName;
    private final String companyRegistrationNumber;
    private final String companyPhoneNumber;
    private final String businessType;
    private final String imageUri;
    private final String verificationStatus;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

    private CompanyRegistrationResponse(UUID id, String name, String address, String representativeName,
                                        String companyRegistrationNumber, String companyPhoneNumber,
                                        String businessType, String imageUri, VerificationStatus verificationStatus,
                                        LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.representativeName = representativeName;
        this.companyRegistrationNumber = companyRegistrationNumber;
        this.companyPhoneNumber = companyPhoneNumber;
        this.businessType = businessType;
        this.imageUri = imageUri;
        this.verificationStatus = verificationStatus.getStatus();
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public static CompanyRegistrationResponse from(CompanyRegistration registration,
                                                   Function<String, String> urlGenerator) {
        return new CompanyRegistrationResponse(
            registration.getRegistrationId(),
            registration.getCompanyName(),
            registration.getRepresentativeName(),
            registration.getCompanyRegistrationNumber(),
            registration.getCompanyAddress(),
            registration.getCompanyPhoneNumber(),
            registration.getBusinessType(),
            urlGenerator.apply(registration.getImageUri()),
            registration.getVerificationStatus(),
            registration.getCreatedAt(),
            registration.getModifiedAt()
        );
    }
}
