package com.aivle.bit.board.dto.request;

import com.aivle.bit.member.domain.Member;

public record BoardCreateRequest(
    String title,
    String content,
    Member member,
    Boolean isSecret
) {

}