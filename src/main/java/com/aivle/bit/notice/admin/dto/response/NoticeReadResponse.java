package com.aivle.bit.notice.admin.dto.response;

import com.aivle.bit.notice.domain.Notice;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoticeReadResponse {

    private Long noticeId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public NoticeReadResponse(final Long noticeId, final String title, final String content,
                              final LocalDateTime createdAt) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public NoticeReadResponse(final Long noticeId, final String title, final String content,
                              final LocalDateTime modifiedAt, boolean isModified) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
        this.modifiedAt = modifiedAt;
    }

    public static NoticeReadResponse fromForCreate(final Notice notice) {
        return new NoticeReadResponse(
            notice.getId(),
            notice.getTitle(),
            notice.getContent(),
            notice.getCreatedAt()
        );
    }

    public static NoticeReadResponse fromForUpdate(final Notice notice) {
        return new NoticeReadResponse(
            notice.getId(),
            notice.getTitle(),
            notice.getContent(),
            notice.getModifiedAt(),
            true
        );
    }
}
