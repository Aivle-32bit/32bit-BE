package com.aivle.bit.notice.member.service;

import com.aivle.bit.notice.admin.dto.response.NoticeReadResponse;
import com.aivle.bit.notice.repository.NoticeRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserNoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public List<NoticeReadResponse> findNoticeAll(Pageable pageable) {
        return noticeRepository.findByIsDeletedFalse(pageable).getContent()
            .stream()
            .map(NoticeReadResponse::fromForCreate)
            .collect(Collectors.toList());
    }
}