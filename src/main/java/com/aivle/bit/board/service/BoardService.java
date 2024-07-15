package com.aivle.bit.board.service;

import static com.aivle.bit.global.exception.ErrorCode.CONTENT_REQUIRED;
import static com.aivle.bit.global.exception.ErrorCode.INVALID_INPUT_VALUE;
import static com.aivle.bit.global.exception.ErrorCode.INVALID_REQUEST;
import static com.aivle.bit.global.exception.ErrorCode.POST_CANNOT_EDIT;
import static com.aivle.bit.global.exception.ErrorCode.POST_FORBIDDEN;
import static com.aivle.bit.global.exception.ErrorCode.POST_NOTFOUND;
import static com.aivle.bit.global.exception.ErrorCode.TITLE_REQUIRED;

import com.aivle.bit.board.domain.Board;
import com.aivle.bit.board.dto.request.BoardCreateRequest;
import com.aivle.bit.board.dto.request.BoardUpdateRequest;
import com.aivle.bit.board.dto.response.BoardReadResponse;
import com.aivle.bit.board.repository.BoardRepository;
import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.member.domain.Member;
import java.util.List;
import java.util.Optional;
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
        validateMember(member);
        validateBoardRequest(boardCreateRequest.title(), boardCreateRequest.content());

        Boolean isSecret = Optional.ofNullable(boardCreateRequest.isSecret()).orElse(false);

        Board board = Board.create(boardCreateRequest.title(), boardCreateRequest.content(), member, isSecret);

        return boardRepository.save(board);
    }

    @Transactional
    public Board updateBoard(Member member, Long boardId, BoardUpdateRequest boardUpdateRequest) {
        validateMember(member);
        validateBoardRequest(boardUpdateRequest.title(), boardUpdateRequest.content());

        Board board = findBoardForUpdate(boardId, member);
        if (board.getParentId() != null) {
            throw new AivleException(POST_CANNOT_EDIT);
        }
        board.isAuthor(member);
        board.update(boardUpdateRequest.title(), boardUpdateRequest.content(), boardUpdateRequest.isSecret());
        return boardRepository.save(board);
    }


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

    private void validateBoardRequest(String title, String content) {
        if ((title == null || title.isBlank()) && (content == null || content.isBlank())) {
            throw new AivleException(INVALID_INPUT_VALUE);
        }
        if (title == null || title.isBlank()) {
            throw new AivleException(TITLE_REQUIRED);
        }
        if (content == null || content.isBlank()) {
            throw new AivleException(CONTENT_REQUIRED);
        }
    }

    @Transactional(readOnly = true)
    public List<BoardReadResponse> findAll(Pageable pageable) {
        return boardRepository.findByIsDeletedFalse(pageable).getContent()
            .stream()
            .map(board -> BoardReadResponse.from(board, board.getMember())) // 반환 형식 변경
            .toList();
    }


    @Transactional(readOnly = true)
    public List<BoardReadResponse> findBoardByTitle(String title) {
        if (title == null || title.isBlank()) {
            return List.of();
        }
        return boardRepository.findByTitleContaining(title)
            .stream()
            .filter(board -> !board.isDeleted())
            .sorted((b1, b2) -> {
                int frequencyComparison = Integer.compare(
                    (int) b2.getTitle().toLowerCase().chars().filter(ch -> ch == title.toLowerCase().charAt(0)).count(),
                    (int) b1.getTitle().toLowerCase().chars().filter(ch -> ch == title.toLowerCase().charAt(0)).count()
                );
                if (frequencyComparison == 0) {
                    return b2.getCreatedAt().compareTo(b1.getCreatedAt());
                }
                return frequencyComparison;
            })
            .map(board -> BoardReadResponse.from(board, board.getMember()))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<BoardReadResponse> findMyBoard(Member member) {
        return boardRepository.findAllByMemberId(member.getId())
            .stream()
            .filter(board -> !board.isDeleted())
            .map(board -> BoardReadResponse.from(board, board.getMember())) // 반환 형식 변경
            .toList();
    }

    @Transactional(readOnly = true)
    public Board findBoardForUpdate(Long boardId, Member member) {
        Board board = boardRepository.findByIdAndIsDeletedFalse(boardId)
            .orElseThrow(() -> new AivleException(POST_NOTFOUND));

        if (!board.canView(member)) {
            throw new AivleException(POST_FORBIDDEN);
        }

        board.incrementViewCount();
        boardRepository.save(board);
        return board;
    }

}