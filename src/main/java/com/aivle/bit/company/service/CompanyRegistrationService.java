package com.aivle.bit.company.service;

import com.aivle.bit.company.domain.CompanyRegistration;
import com.aivle.bit.company.dto.request.CompanyRegistrationRequest;
import com.aivle.bit.company.dto.response.CompanyRegistrationResponse;
import com.aivle.bit.company.repository.CompanyRegistrationRepository;
import com.aivle.bit.member.domain.Member;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyRegistrationService {

    private final CompanyRegistrationRepository companyRegistrationRepository;
    private final S3Service s3Service;

    public void save(CompanyRegistrationRequest request, Member member) {
        String key = s3Service.uploadImage(request.image(),
            "company-registration/image");

        CompanyRegistration companyRegistration = CompanyRegistration.of(request.companyName(),
            request.representativeName(), request.companyRegistrationNumber(), request.companyAddress(),
            request.companyPhoneNumber(), request.businessType(), key, member);

        companyRegistrationRepository.save(companyRegistration);
    }

    public List<CompanyRegistrationResponse> findAllByMember(Member member) {
        return companyRegistrationRepository.findAllByMember(member)
            .stream()
            .map(registration -> CompanyRegistrationResponse.from(registration, s3Service::generatePresignedUrl))
            .collect(Collectors.toList());
    }
}
