package com.aivle.bit.board.service;

import static com.aivle.bit.global.exception.ErrorCode.CONTENT_REQUIRED;
import static com.aivle.bit.global.exception.ErrorCode.PASSWORD_REQUIRED_FOR_SECRET_POST;
import static com.aivle.bit.global.exception.ErrorCode.TITLE_REQUIRED;

import com.aivle.bit.board.domain.Board;
import com.aivle.bit.board.dto.request.BoardCreateRequest;
import com.aivle.bit.board.repository.BoardRepository;
import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.member.domain.Member;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Board createBoard(Member member, BoardCreateRequest boardCreateRequest) {
        if (boardCreateRequest.getTitle() == null || boardCreateRequest.getTitle().isEmpty()) {
            throw new AivleException(TITLE_REQUIRED);
        }
        if (boardCreateRequest.getContent() == null || boardCreateRequest.getContent().isEmpty()) {
            throw new AivleException(CONTENT_REQUIRED);
        }
        if (boardCreateRequest.isSecret() && (boardCreateRequest.getBoardpw() == null || boardCreateRequest.getBoardpw().isEmpty())) {
            throw new AivleException(PASSWORD_REQUIRED_FOR_SECRET_POST);
        }
        // 빌더 패턴을 사용하여 Board 객체 생성
        Board.BoardBuilder boardBuilder = Board.builder()
            .title(boardCreateRequest.getTitle())
            .content(boardCreateRequest.getContent())
            .memberId(member.getId())
            .state(0)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now());

        if (boardCreateRequest.isSecret()) {
            boardBuilder.secret(true)
                .boardpw(boardCreateRequest.getBoardpw());
        }

        Board board = boardBuilder.build();
        return boardRepository.save(board);
    }
}