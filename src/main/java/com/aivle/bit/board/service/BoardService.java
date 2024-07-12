package com.aivle.bit.board.service;

import static com.aivle.bit.global.exception.ErrorCode.CONTENT_REQUIRED;
import static com.aivle.bit.global.exception.ErrorCode.POST_FORBIDDEN;
import static com.aivle.bit.global.exception.ErrorCode.POST_NOTFOUND;
import static com.aivle.bit.global.exception.ErrorCode.TITLE_REQUIRED;

import com.aivle.bit.board.domain.Board;
import com.aivle.bit.board.dto.request.BoardCreateRequest;
import com.aivle.bit.board.repository.BoardRepository;
import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.member.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Board createBoard(Member member, BoardCreateRequest boardCreateRequest) {
        if (boardCreateRequest.title() == null || boardCreateRequest.title().isEmpty()) {
            throw new AivleException(TITLE_REQUIRED);
        }
        if (boardCreateRequest.content() == null || boardCreateRequest.content().isEmpty()) {
            throw new AivleException(CONTENT_REQUIRED);
        }

        Board board = Board.builder()
            .title(boardCreateRequest.title())
            .content(boardCreateRequest.content())
            .member(member)
            .parentId(null)
            .isDeleted(false)
            .isSecret(boardCreateRequest.isSecret())
            .build();

        return boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public List<Board> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    public Board findBoard(Long boardId, Member member) {
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new AivleException(POST_NOTFOUND));
        if (!board.canView(member)) {
            throw new AivleException(POST_FORBIDDEN);
        }
        return board;
    }


    @Transactional(readOnly = true)
    public List<Board> findBoardByTitle(String title) {
        return boardRepository.findByTitleContaining(title);
    }


    @Transactional(readOnly = true)
    public List<Board> findMyBoard(Member member) {
        return boardRepository.findAllByMemberId(member.getId());
    }
}