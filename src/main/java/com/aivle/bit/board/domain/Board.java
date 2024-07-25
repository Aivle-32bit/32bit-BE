package com.aivle.bit.board.domain;

import static com.aivle.bit.global.exception.ErrorCode.BOARD_AUTHOR_ONLY_EXCEPTION;
import static com.aivle.bit.global.exception.ErrorCode.INVALID_REPLY_REQUEST;
import static com.aivle.bit.global.exception.ErrorCode.POST_FORBIDDEN;

import com.aivle.bit.global.domain.BaseTimeEntity;
import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Comment("제목")
    private String title;

    @Column(nullable = false)
    @Comment("내용")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    @Comment("회원 ID")
    private Member member;

    @Column
    @Comment("질문글에 대한 id")
    private Long parentId;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @Comment("True-삭제, False-삭제 아님")
    private Boolean isDeleted;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @Comment("True - 비밀글, False - 공개글")
    private Boolean isSecret;

    @Column(nullable = false)
    @Comment("조회수")
    private int viewCount;

    private Board(String title, String content, Member member, Long parentId, Boolean isDeleted,
                  Boolean isSecret, int viewCount) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.parentId = parentId;
        this.isDeleted = isDeleted;
        this.isSecret = isSecret;
        this.viewCount = viewCount;
    }

    public static Board create(String title, String content, Member member, Boolean isSecret) {
        return new Board(title, content, member, null, false, isSecret, 0);
    }

    public static Board reply(String content, Member member, Board parentBoard) {
        Boolean isSecret = parentBoard.isSecret;
        if (parentBoard.parentId != null) {
            throw new AivleException(INVALID_REPLY_REQUEST);
        }
        return new Board("답글", content, member, parentBoard.getId(), false, isSecret, 0);
    }

    public void update(String title, String content, Boolean isSecret) {
        this.title = title;
        this.content = content;
        this.isSecret = isSecret;
        this.setModifiedAt(LocalDateTime.now());
    }


    public void isAuthor(Member member) {
        boolean isAdmin = member.getIsAdmin();

        if (isAdmin) {
            return;
        }

        if (!this.member.getId().equals(member.getId())) {
            throw new AivleException(BOARD_AUTHOR_ONLY_EXCEPTION);
        }
    }

    public void canView(Member member, Board parentBoard) {
        boolean isSameMember = this.member.getId().equals(member.getId());
        boolean isSecret = this.isSecret;
        boolean isAdmin = member.getIsAdmin();

        if (!isSecret) {
            return;
        }

        if (isAdmin) {
            return;
        }

        if (isSameMember) {
            return;
        }

        if (parentBoard != null && parentBoard.member.getId().equals(member.getId())) {
            return;
        }

        throw new AivleException(POST_FORBIDDEN);
    }


    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void markAsDeleted() {
        this.isDeleted = true;
    }
}
