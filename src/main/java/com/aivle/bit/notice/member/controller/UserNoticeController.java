package com.aivle.bit.notice.member.controller;

import com.aivle.bit.notice.admin.dto.response.NoticeReadResponse;
import com.aivle.bit.notice.member.service.UserNoticeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
public class UserNoticeController {

    private final UserNoticeService noticeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NoticeReadResponse> findNoticeAll(Pageable pageable) {
        return noticeService.findNoticeAll(pageable);
    }
}