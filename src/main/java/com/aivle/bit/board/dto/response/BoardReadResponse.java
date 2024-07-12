package com.aivle.bit.board.dto.response;

import com.aivle.bit.board.domain.Board;
import com.aivle.bit.global.domain.BaseTimeEntity;
import com.aivle.bit.member.domain.Member;
import lombok.Getter;

@Getter
public class BoardReadResponse extends BaseTimeEntity {

    private Long boardId;
    private String title;
    private String content;
    private Member member;

    private BoardReadResponse() {
    }

    public BoardReadResponse(final Long boardId, final String title, final String content, final Member member) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public static BoardReadResponse from(final Board board) {
        return new BoardReadResponse(board.getId(), board.getTitle(), board.getContent(), board.getMember());
    }
}
