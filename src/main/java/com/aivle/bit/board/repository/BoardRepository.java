package com.aivle.bit.board.repository;

import com.aivle.bit.board.domain.Board;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByTitleContaining(String title);

    List<Board> findAllByMemberId(Long memberId);
}