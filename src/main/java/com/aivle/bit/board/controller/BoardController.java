package com.aivle.bit.board.controller;


import com.aivle.bit.auth.jwt.JwtLogin;
import com.aivle.bit.board.domain.Board;
import com.aivle.bit.board.dto.request.BoardCreateRequest;
import com.aivle.bit.board.dto.request.BoardUpdateRequest;
import com.aivle.bit.board.dto.response.BoardReadResponse;
import com.aivle.bit.board.service.BoardService;
import com.aivle.bit.member.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {


    private final BoardService boardService;

    @Comment("게시글 등록")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoardReadResponse createBoard(@RequestBody BoardCreateRequest boardCreateRequest,
    @JwtLogin Member member) {
        Board board = boardService.createBoard(member, boardCreateRequest);
        return BoardReadResponse.from(board, member);
    }

    @Comment("게시글 수정")
    @PutMapping("/{boardId}")
    @ResponseStatus(HttpStatus.OK)
    public BoardReadResponse updateBoard(@JwtLogin Member member, @PathVariable Long boardId,
                                         @RequestBody BoardUpdateRequest boardUpdateRequest) {
        Board updatedBoard = boardService.updateBoard(member, boardId, boardUpdateRequest);
        return BoardReadResponse.from(updatedBoard, member);
    }

    @Comment("게시글 삭제")
    @DeleteMapping("/{boardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBoard(@JwtLogin Member member, @PathVariable Long boardId) {
        boardService.deleteBoard(member, boardId);
    }

    @Comment("비밀글이면 작성자가 맞는지 확인")
    @GetMapping("/{boardId}")
    @ResponseStatus(HttpStatus.OK)
    public BoardReadResponse findBoard(@PathVariable Long boardId, @JwtLogin Member member) {
        Board board = boardService.findBoardForUpdate(boardId, member);
        return BoardReadResponse.from(board, member);
    }

    @Comment("글 목록")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BoardReadResponse> findAll(Pageable pageable) {
        return boardService.findAll(pageable);
    }

    @Comment("제목으로 검색")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<BoardReadResponse> findBoardByTitle(@RequestParam String title) {
        return boardService.findBoardByTitle(title);
    }

    @Comment("내가 쓴 게시글 조회")
    @GetMapping("/my_boards")
    @ResponseStatus(HttpStatus.OK)
    public List<BoardReadResponse> findMyBoard(@JwtLogin Member member) {
        return boardService.findMyBoard(member);
    }

}