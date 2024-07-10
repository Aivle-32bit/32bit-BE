package com.aivle.bit.board.dto.request;


import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BoardRequest {
    private Long id;
    private String title;
    private String content;
    private Long memberId;
    private int state;   //삭제 여부
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean secret;  //비밀글
    private String boardpw;  //비밀글 pw

    public BoardRequest() {}

    @Builder
    public BoardRequest(Long id, String title, String content, Long memberId, int state,
                        LocalDateTime createdAt, LocalDateTime updatedAt, boolean secret, String boardpw) {
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
}