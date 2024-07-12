package com.aivle.bit.board.controller;


import com.aivle.bit.auth.jwt.JwtLogin;
import com.aivle.bit.board.domain.Board;
import com.aivle.bit.board.dto.request.BoardCreateRequest;
import com.aivle.bit.board.dto.response.BoardReadResponse;
import com.aivle.bit.board.service.BoardService;
import com.aivle.bit.member.domain.Member;
import java.util.List;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Board> createBoard(@RequestBody BoardCreateRequest boardCreateRequest,
                                             @JwtLogin Member member) {
        Board createdBoard = boardService.createBoard(member, boardCreateRequest);
        return ResponseEntity.ok(createdBoard);
    }

    @Comment("글 목록")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Board> findAll(Pageable pageable) {
        return boardService.findAll(pageable);
    }

    @Comment("비밀글이면 작성자가 맞는지 확인")
    @GetMapping("/{boardId}")
    @ResponseStatus(HttpStatus.OK)
    public BoardReadResponse findBoard(@PathVariable Long boardId, @JwtLogin Member member) {
        Board board = boardService.findBoard(boardId, member);
        return BoardReadResponse.from(board);
    }

    @Comment("제목으로 검색")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Board> findBoardByTitle(@RequestParam String title) {
        return boardService.findBoardByTitle(title);
    }

    @Comment("내가 쓴 게시글 조회")
    @GetMapping("/my_boards")
    @ResponseStatus(HttpStatus.OK)
    public List<Board> findMyBoard(@JwtLogin Member member) {
        return boardService.findMyBoard(member);
    }
}
