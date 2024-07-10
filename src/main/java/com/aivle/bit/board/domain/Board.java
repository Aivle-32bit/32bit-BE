package com.aivle.bit.board.domain;

import static ch.qos.logback.classic.spi.ThrowableProxyVO.build;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private Long memberId;
    private int state;   //삭제 여부
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean secret;  //비밀글
    private String boardpw;  //비밀글 pw

    @Builder
    private Board(Long id, String title, String content, Long memberId, int state,
                  LocalDateTime createdAt,
                  LocalDateTime updatedAt,
                  boolean secret, String boardpw) {

        this.id = id;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.secret = secret;
        this.boardpw = boardpw;
    }

    public static Board of(String title, String content, Long memberId) {
        return Board.builder()
            .title(title)
            .content(content)
            .memberId(memberId)
            .state(0)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .secret(false)
            .boardpw(null)
            .build();
    }
}