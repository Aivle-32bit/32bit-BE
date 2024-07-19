package com.aivle.bit.board.dto.response;

import com.aivle.bit.board.domain.Board;
import com.aivle.bit.member.domain.Member;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoardListResponse {

    private Long boardId;
    private Long parentId;
    private String title;
    private String content;
    private int viewCount;
    private Long memberId;
    private String memberName;
    private Boolean isSecret;
    private LocalDateTime createdAt;

    public BoardListResponse(final Long boardId, final Long parentId, final String title, final String content,
                             final int viewCount, final Long memberId, final String memberName, final Boolean isSecret,
                             final LocalDateTime createdAt) {
        this.boardId = boardId;
        this.parentId = parentId;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.memberId = memberId;
        this.memberName = memberName;
        this.isSecret = isSecret;
        this.createdAt = createdAt;
    }

    public static BoardListResponse from(final Board board) {
        return new BoardListResponse(
            board.getId(),
            board.getParentId(),
            board.getTitle(),
            board.getContent(),
            board.getViewCount(),
            board.getMember().getId(),
            board.getMember().getName(),
            board.getIsSecret(),
            board.getCreatedAt()
        );
    }
}
