package com.aivle.bit.notice.admin.service;

import static com.aivle.bit.global.exception.ErrorCode.POST_FORBIDDEN;
import static com.aivle.bit.global.exception.ErrorCode.POST_NOT_FOUND;
import static com.aivle.bit.global.utils.ValidationUtil.validateTitleAndContent;

import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.member.domain.Member;
import com.aivle.bit.notice.admin.dto.request.NoticeCreateRequest;
import com.aivle.bit.notice.admin.dto.request.NoticeUpdateRequest;
import com.aivle.bit.notice.admin.dto.response.NoticeReadResponse;
import com.aivle.bit.notice.domain.Notice;
import com.aivle.bit.notice.repository.NoticeRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminNoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public NoticeReadResponse createNotice(Member member, NoticeCreateRequest noticeCreateRequest) {
        validateAdmin(member);
        validateTitleAndContent(noticeCreateRequest.title(), noticeCreateRequest.content());
        Notice notice = Notice.create(noticeCreateRequest.title(), noticeCreateRequest.content(), member);
        notice = noticeRepository.save(notice);
        return NoticeReadResponse.fromForCreate(notice);
    }

    @Transactional
    public NoticeReadResponse updateNotice(Member member, Long noticeId, NoticeUpdateRequest noticeUpdateRequest) {
        validateAdmin(member);
        validateTitleAndContent(noticeUpdateRequest.title(), noticeUpdateRequest.content());
        Notice notice = findNoticeForUpdate(noticeId);
        notice.update(noticeUpdateRequest.title(), noticeUpdateRequest.content());
        notice = noticeRepository.save(notice);
        return NoticeReadResponse.fromForUpdate(notice);
    }

    public void deleteNotice(Member member, Long noticeId) {
        validateAdmin(member);
        Notice notice = findNoticeForUpdate(noticeId);
        notice.markAsDeleted();
        noticeRepository.save(notice);
    }

    @Transactional(readOnly = true)
    public List<NoticeReadResponse> findNoticeAll(Pageable pageable) {
        return noticeRepository.findByIsDeletedFalse(pageable).getContent()
            .stream()
            .map(NoticeReadResponse::fromForCreate)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Notice findNoticeForUpdate(Long noticeId) {
        return noticeRepository.findByIdAndIsDeletedFalse(noticeId)
            .orElseThrow(() -> new AivleException(POST_NOT_FOUND));
    }

    private void validateAdmin(Member member) {
        if (member == null || !member.isAdmin()) {
            throw new AivleException(POST_FORBIDDEN);
        }
    }
}
