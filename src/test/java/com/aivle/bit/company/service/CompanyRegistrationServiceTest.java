package com.aivle.bit.company.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aivle.bit.company.domain.CompanyRegistration;
import com.aivle.bit.company.domain.VerificationStatus;
import com.aivle.bit.company.dto.request.CompanyRegistrationRequest;
import com.aivle.bit.company.dto.response.CompanyRegistrationResponse;
import com.aivle.bit.company.repository.CompanyRegistrationRepository;
import com.aivle.bit.member.domain.Member;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class CompanyRegistrationServiceTest {

    @Mock
    private CompanyRegistrationRepository companyRegistrationRepository;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private CompanyRegistrationService companyRegistrationService;

    private Member member;
    private CompanyRegistrationRequest request;
    private CompanyRegistration companyRegistration;

    @BeforeEach
    void setUp() {
        member = Member.of("Fighting32@aivle.site", "Woanxkawjd1!", "최정원", "창원시 정원구 다이노스동 123-45");

        MultipartFile image = mock(MultipartFile.class);

        request = new CompanyRegistrationRequest(
            "Test Company",
            "John Doe",
            "123-45-67890",
            "123 Test Street",
            "010-1234-5678",
            "Technology",
            image
        );

        companyRegistration = CompanyRegistration.builder()
            .companyName(request.companyName())
            .representativeName(request.representativeName())
            .companyRegistrationNumber(request.companyRegistrationNumber())
            .companyAddress(request.companyAddress())
            .companyPhoneNumber(request.companyPhoneNumber())
            .businessType(request.businessType())
            .imageUri("s3://bucket/test-image.jpg")
            .verificationStatus(VerificationStatus.PENDING)
            .member(member)
            .build();
    }

    @Test
    @DisplayName("[성공] 회사 등록을 성공하면 회사 등록이 정상적으로 저장된다.")
    void save_shouldSaveCompanyRegistration() {
        when(s3Service.uploadImage(any(MultipartFile.class), anyString())).thenReturn("s3://bucket/test-image.jpg");
        when(companyRegistrationRepository.save(any(CompanyRegistration.class))).thenReturn(companyRegistration);

        companyRegistrationService.save(request, member);

        verify(s3Service, times(1)).uploadImage(any(MultipartFile.class), anyString());
        verify(companyRegistrationRepository, times(1)).save(any(CompanyRegistration.class));
    }

    @Test
    @DisplayName("[성공] 회원이 등록한 회사 등록을 조회하면 회사 등록이 정상적으로 반환된다.")
    void findAllByMember_shouldReturnCompanyRegistrations() {
        List<CompanyRegistration> registrations = Arrays.asList(companyRegistration);
        when(companyRegistrationRepository.findAllByMember(any(Member.class))).thenReturn(registrations);
        when(s3Service.generatePresignedUrl(anyString())).thenReturn("http://presigned-url.com/test-image.jpg");

        List<CompanyRegistrationResponse> responses = companyRegistrationService.findAllByMember(member);

        assertEquals(1, responses.size());
        assertEquals("Test Company", responses.get(0).getCompanyName());
        verify(companyRegistrationRepository, times(1)).findAllByMember(any(Member.class));
    }
}
