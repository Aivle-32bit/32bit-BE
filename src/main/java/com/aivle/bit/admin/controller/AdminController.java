package com.aivle.bit.admin.controller;

import com.aivle.bit.admin.dto.request.RejectRequest;
import com.aivle.bit.admin.dto.response.MemberResponse;
import com.aivle.bit.admin.service.AdminService;
import com.aivle.bit.auth.jwt.Admin;
import com.aivle.bit.company.dto.response.CompanyRegistrationResponse;
import com.aivle.bit.member.domain.Member;
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
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public void getDashboard(@Admin Member member) {
        // 관리자 대시보드
    }

    @GetMapping("/members")
    @ResponseStatus(HttpStatus.OK)
    public List<MemberResponse> manageUsers(
        @RequestParam(required = false) MemberState state) {
        return adminService.findAllMember(state);
    }

    @GetMapping("/members/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<CompanyRegistrationResponse> getCompanyRegistrations(@PathVariable Long id) {
        return adminService.findCompanyRegistrations(id);
    }

    @PostMapping("/members/{id}/approve")
    @ResponseStatus(HttpStatus.OK)
    public void approveUser(@PathVariable UUID id) {
        adminService.approveUser(id);
    }

    @PostMapping("/members/{id}/reject")
    @ResponseStatus(HttpStatus.OK)
    public void rejectUser(@PathVariable UUID id, @RequestBody @Valid RejectRequest request) {
        adminService.rejectUser(id, request.reason());
    }

    @PostMapping("/members/{id}/dormant")
    @ResponseStatus(HttpStatus.OK)
    public void updateDormant(@PathVariable Long id) {
        adminService.updateDormant(id);
    }

    @DeleteMapping("/members/{id}")
    public void deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
    }
}
