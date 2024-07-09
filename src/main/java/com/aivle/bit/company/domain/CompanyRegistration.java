package com.aivle.bit.company.domain;

import static com.aivle.bit.global.exception.ErrorCode.INVALID_BUSINESS_REGISTRATION_NUMBER_FORMAT;
import static com.aivle.bit.global.exception.ErrorCode.INVALID_PHONE_NUMBER_FORMAT;

import com.aivle.bit.global.domain.BaseTimeEntity;
import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyRegistration extends BaseTimeEntity {

    private static final String BUSINESS_NUMBER_REGEX = "^\\d{3}-\\d{2}-\\d{5}$";
    private static final String PHONE_NUMBER_REGEX = "^(01[016789]-\\d{3,4}-\\d{4}|\\d{2,3}-\\d{3,4}-\\d{4})$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UuidGenerator
    @Column(nullable = false, unique = true)
    @Comment("등록 ID")
    private UUID registrationId;

    @Column(nullable = false)
    @Comment("회사 이름")
    private String companyName;

    @Column(nullable = false)
    @Comment("대표자명")
    private String representativeName;

    @Column(nullable = false)
    @Comment("사업자 등록 번호")
    private String companyRegistrationNumber;

    @Column(nullable = false)
    @Comment("회사 주소")
    private String companyAddress;

    @Column(nullable = false)
    @Comment("회사 전화번호")
    private String companyPhoneNumber;

    @Column(nullable = false)
    @Comment("사업 종류")
    private String businessType;

    @Column(nullable = false)
    @Comment("사업자 등록증 이미지 URL")
    private String imageUri;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("인증 상태")
    private VerificationStatus verificationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @Comment("회원 ID")
    private Member member;

    @Builder
    private CompanyRegistration(final String companyName, final String representativeName,
                                final String companyRegistrationNumber, final String companyAddress,
                                final String companyPhoneNumber, final String businessType,
                                final String imageUri, final VerificationStatus verificationStatus,
                                final Member member) {
        validate(companyRegistrationNumber, companyPhoneNumber);
        this.companyName = companyName;
        this.representativeName = representativeName;
        this.companyRegistrationNumber = companyRegistrationNumber;
        this.companyAddress = companyAddress;
        this.companyPhoneNumber = companyPhoneNumber;
        this.businessType = businessType;
        this.imageUri = imageUri;
        this.verificationStatus = verificationStatus != null ? verificationStatus : VerificationStatus.PENDING;
        this.member = member;
    }

    public static CompanyRegistration of(final String companyName, final String representativeName,
                                         final String companyRegistrationNumber, final String companyAddress,
                                         final String companyPhoneNumber, final String businessType,
                                         final String imageUri, final Member member) {
        return CompanyRegistration.builder()
            .companyName(companyName)
            .representativeName(representativeName)
            .companyRegistrationNumber(companyRegistrationNumber)
            .companyAddress(companyAddress)
            .companyPhoneNumber(companyPhoneNumber)
            .businessType(businessType)
            .imageUri(imageUri)
            .verificationStatus(VerificationStatus.PENDING)
            .member(member)
            .build();
    }

    private void validate(final String companyRegistrationNumber, final String companyPhoneNumber) {
        if (!companyRegistrationNumber.matches(BUSINESS_NUMBER_REGEX)) {
            throw new AivleException(INVALID_BUSINESS_REGISTRATION_NUMBER_FORMAT);
        }

        if (!companyPhoneNumber.matches(PHONE_NUMBER_REGEX)) {
            throw new AivleException(INVALID_PHONE_NUMBER_FORMAT);
        }
    }
}
