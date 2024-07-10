package com.aivle.bit.board.dto.request;


import java.time.LocalDateTime;
import lombok.Getter;

@Getter
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

    // 매개변수 있는 생성자
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserId(Long memberId) {
        this.memberId = memberId;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setSecret(boolean secret) {
        this.secret = secret;
    }

    public void setBoardpw(String boardpw) {
        this.boardpw = boardpw;
    }
}
