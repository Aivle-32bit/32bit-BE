package com.aivle.bit.notice.admin.controller;

import com.aivle.bit.auth.jwt.JwtLogin;
import com.aivle.bit.member.domain.Member;
import com.aivle.bit.notice.admin.dto.request.NoticeCreateRequest;
import com.aivle.bit.notice.admin.dto.request.NoticeUpdateRequest;
import com.aivle.bit.notice.admin.dto.response.NoticeReadResponse;
import com.aivle.bit.notice.admin.service.AdminNoticeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminNoticeController {

    private final AdminNoticeService noticeService;

    @Comment("공지사항 등록")
    @PostMapping("/admin/notice")
    @ResponseStatus(HttpStatus.CREATED)
    public NoticeReadResponse createNotice(@JwtLogin Member member,
                                           @RequestBody NoticeCreateRequest noticeCreateRequest) {
        return noticeService.createNotice(member, noticeCreateRequest);
    }

    @Comment("공지사항 수정")
    @PutMapping("/admin/notice/{noticeId}")
    @ResponseStatus(HttpStatus.OK)
    public NoticeReadResponse updateNotice(@JwtLogin Member member, @PathVariable Long noticeId,
                                           @RequestBody NoticeUpdateRequest noticeUpdateRequest) {
        return noticeService.updateNotice(member, noticeId, noticeUpdateRequest);
    }

    @Comment("공지사항 삭제")
    @DeleteMapping("/admin/notice/{noticeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotice(@JwtLogin Member member, @PathVariable Long noticeId) {
        noticeService.deleteNotice(member, noticeId);
    }

    @Comment("공지사항 목록")
    @GetMapping("/notice")
    @ResponseStatus(HttpStatus.OK)
    public List<NoticeReadResponse> findNoticeAll(Pageable pageable) {
        return noticeService.findNoticeAll(pageable);
    }
}