package com.aivle.bit.company.dto.response;

import com.aivle.bit.company.domain.CompanyRegistration;
import com.aivle.bit.company.domain.VerificationStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Function;
import lombok.Getter;

@Getter
public class CompanyRegistrationResponse {

    private final UUID registrationId;
    private final String companyName;
    private final String representativeName;
    private final String companyRegistrationNumber;
    private final String companyAddress;
    private final String companyPhoneNumber;
    private final String businessType;
    private final String imageUri;
    private final VerificationStatus verificationStatus;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

    private CompanyRegistrationResponse(UUID registrationId, String companyName, String representativeName,
                                        String companyRegistrationNumber, String companyAddress,
                                        String companyPhoneNumber, String businessType, String imageUri,
                                        VerificationStatus verificationStatus, LocalDateTime createdDate,
                                        LocalDateTime lastModifiedDate) {
        this.registrationId = registrationId;
        this.companyName = companyName;
        this.representativeName = representativeName;
        this.companyRegistrationNumber = companyRegistrationNumber;
        this.companyAddress = companyAddress;
        this.companyPhoneNumber = companyPhoneNumber;
        this.businessType = businessType;
        this.imageUri = imageUri;
        this.verificationStatus = verificationStatus;
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
