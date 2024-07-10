package com.aivle.bit.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardCreateRequest {

    private String title;
    private String content;
    private boolean secret;
    private String boardpw;
}
