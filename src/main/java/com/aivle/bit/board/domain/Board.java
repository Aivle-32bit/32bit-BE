package com.aivle.bit.board.domain;

import com.aivle.bit.global.domain.BaseTimeEntity;
import com.aivle.bit.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
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

    @ManyToOne(fetch = FetchType.LAZY)
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

    @Builder
    public Board(Long id, String title, String content, Member member, Long parentId, Boolean isDeleted,
                 Boolean isSecret, int viewCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.member = member;
        this.parentId = parentId;
        this.isDeleted = isDeleted;
        this.isSecret = isSecret;
        this.viewCount = viewCount;
    }

    public static Board of(String title, String content, Member member) {
        return Board.builder()
            .title(title)
            .content(content)
            .member(member)
            .parentId(null)
            .isDeleted(false)
            .isSecret(false)
            .viewCount(0)
            .build();
    }

    public boolean canView(Member member) {
        return !this.isSecret || this.member.equals(member);
    }

    public void incrementViewCount() {
        this.viewCount++;
    }
}