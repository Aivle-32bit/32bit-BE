package com.aivle.bit.board.service;

import static com.aivle.bit.global.exception.ErrorCode.INVALID_REQUEST;
import static com.aivle.bit.global.exception.ErrorCode.POST_CANNOT_EDIT;
import static com.aivle.bit.global.exception.ErrorCode.POST_NOT_FOUND;

import com.aivle.bit.board.domain.Board;
import com.aivle.bit.board.dto.request.BoardCreateRequest;
import com.aivle.bit.board.dto.request.BoardUpdateRequest;
import com.aivle.bit.board.dto.request.ReplyCreateRequest;
import com.aivle.bit.board.dto.response.BoardListResponse;
import com.aivle.bit.board.repository.BoardRepository;
import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.member.domain.Member;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Board createBoard(Member member, BoardCreateRequest boardCreateRequest) {
        validateMember(member);

        Boolean isSecret = Optional.ofNullable(boardCreateRequest.isSecret()).orElse(false);

        Board board = Board.create(boardCreateRequest.title(), boardCreateRequest.content(), member, isSecret);

        return boardRepository.save(board);
    }

    @Transactional
    public Board updateBoard(Member member, Long boardId, BoardUpdateRequest boardUpdateRequest) {
        validateMember(member);

        Board board = findBoardForUpdate(boardId, member);
        if (board.getParentId() != null) {
            throw new AivleException(POST_CANNOT_EDIT);
        }
        board.isAuthor(member);
        board.update(boardUpdateRequest.title(), boardUpdateRequest.content(), boardUpdateRequest.isSecret());
        return boardRepository.save(board);
    }

    @Transactional
    public void deleteBoard(Member member, Long boardId) {
        Board board = findBoardForUpdate(boardId, member);

        board.isAuthor(member);
        board.markAsDeleted();
        boardRepository.save(board);
    }

    private void validateMember(Member member) {
        if (member == null) {
            throw new AivleException(INVALID_REQUEST);
        }
    }

    @Transactional(readOnly = true)
    public Page<BoardListResponse> findAll(Pageable pageable) {
        Page<Board> boardsPage = boardRepository.findByIsDeletedFalseOrderByCreatedAtDesc(pageable);
        List<BoardListResponse> boardResponses = boardsPage.getContent().stream()
            .map(BoardListResponse::from)
            .toList();

        return new PageImpl<>(boardResponses, pageable, boardsPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<BoardListResponse> findBoardByTitle(String title, Pageable pageable) {
        Page<Board> boardsPage = boardRepository.findByTitleContainingAndIsDeletedFalseOrderByCreatedAtDesc(title,
            pageable);
        List<BoardListResponse> boardResponses = boardsPage.getContent().stream()
            .map(BoardListResponse::from)
            .toList();

        return new PageImpl<>(boardResponses, pageable, boardsPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<BoardListResponse> findMyBoard(Member member, Pageable pageable) {
        Page<Board> boardsPage = boardRepository.findAllByMemberIdAndIsDeletedFalseOrderByCreatedAtDesc(member.getId(),
            pageable);
        List<BoardListResponse> boardResponses = boardsPage.getContent().stream()
            .map(BoardListResponse::from)
            .toList();

        return new PageImpl<>(boardResponses, pageable, boardsPage.getTotalElements());
    }

    @Transactional
    public Board findBoardForUpdate(Long boardId, Member member) {
        Board board = boardRepository.findByIdAndIsDeletedFalse(boardId)
            .orElseThrow(() -> new AivleException(POST_NOT_FOUND));

        Board parentBoard = null;
        if (board.getParentId() != null) {
            parentBoard = boardRepository.findByIdAndIsDeletedFalse(board.getParentId())
                .orElseThrow(() -> new AivleException(POST_NOT_FOUND));
        }

        board.canView(member, parentBoard);

        board.incrementViewCount();
        boardRepository.save(board);
        return board;
    }


    public Board createReply(Long boardId, Member member, ReplyCreateRequest replyCreateRequest) {
        Board parentBoard = boardRepository.findByIdAndIsDeletedFalse(boardId)
            .orElseThrow(() -> new AivleException(POST_NOT_FOUND));

        Board reply = Board.reply(replyCreateRequest.content(), member, parentBoard);

        return boardRepository.save(reply);
    }
}
