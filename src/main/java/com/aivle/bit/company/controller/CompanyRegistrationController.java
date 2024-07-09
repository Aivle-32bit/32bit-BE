package com.aivle.bit.company.controller;

import com.aivle.bit.auth.jwt.AllowUnverifiedUser;
import com.aivle.bit.company.dto.request.CompanyRegistrationRequest;
import com.aivle.bit.company.dto.response.CompanyRegistrationResponse;
import com.aivle.bit.company.service.CompanyRegistrationService;
import com.aivle.bit.member.domain.Member;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/company-registrations")
public class CompanyRegistrationController {

    private final CompanyRegistrationService companyRegistrationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerCompanyRegistration(
        @ModelAttribute @Valid CompanyRegistrationRequest companyRegistrationRequest,
        @AllowUnverifiedUser Member member) {
        companyRegistrationService.save(companyRegistrationRequest, member);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompanyRegistrationResponse> getAllRegistrationsForMember(@AllowUnverifiedUser Member member) {
        return companyRegistrationService.findAllByMember(member);
    }
}
