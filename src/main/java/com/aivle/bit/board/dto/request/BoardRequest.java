package com.aivle.bit.board.dto.request;


import com.aivle.bit.global.domain.BaseTimeEntity;
import com.aivle.bit.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardRequest extends BaseTimeEntity {

    private Long id;
    private String title;
    private String content;
    private Member member;
    private Long boardId;
    private Boolean isDeleted;
    private Boolean isSecret;
}