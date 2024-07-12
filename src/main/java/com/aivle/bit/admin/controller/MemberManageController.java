package com.aivle.bit.admin.controller;

import com.aivle.bit.admin.dto.request.RejectRequest;
import com.aivle.bit.admin.dto.response.MemberResponse;
import com.aivle.bit.admin.service.MemberManageService;
import com.aivle.bit.company.dto.response.CompanyRegistrationResponse;
import com.aivle.bit.member.domain.MemberState;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/members")
@Slf4j
public class MemberManageController {

    private final MemberManageService memberManageService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MemberResponse> manageUsers(@RequestParam(required = false) MemberState state) {
        log.info("Fetching members with state: {}", state);
        return memberManageService.findAllMember(state);
    }

    @GetMapping("/{id}/latest-registration")
    @ResponseStatus(HttpStatus.OK)
    public CompanyRegistrationResponse getLatestCompanyRegistration(@PathVariable Long id) {
        log.info("Fetching the most recent company registration for member ID: {}", id);
        return memberManageService.findLatestByMember(id);
    }

    @PostMapping("/{id}/approve")
    @ResponseStatus(HttpStatus.OK)
    public void approveUser(@PathVariable UUID id) {
        log.info("Approving member with ID: {}", id);
        memberManageService.approveUser(id);
    }

    @PostMapping("/{id}/reject")
    @ResponseStatus(HttpStatus.OK)
    public void rejectUser(@PathVariable UUID id, @RequestBody @Valid RejectRequest request) {
        log.info("Rejecting member with ID: {}. Reason: {}", id, request.reason());
        memberManageService.rejectUser(id, request.reason());
    }

    @PostMapping("/{id}/dormant")
    @ResponseStatus(HttpStatus.OK)
    public void updateDormant(@PathVariable Long id) {
        log.info("Updating member with ID: {} to dormant", id);
        memberManageService.updateDormant(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Deleting member with ID: {}", id);
        memberManageService.deleteUser(id);
    }
}
