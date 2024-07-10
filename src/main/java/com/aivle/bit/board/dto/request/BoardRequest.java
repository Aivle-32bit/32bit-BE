package com.aivle.bit.board.dto.request;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
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
}