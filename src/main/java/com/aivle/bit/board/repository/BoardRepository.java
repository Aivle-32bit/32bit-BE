package com.aivle.bit.board.repository;

import com.aivle.bit.board.domain.Board;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

    List<Board> findByTitleContaining(String title);

    List<Board> findAllByMemberId(Long memberId);

    Optional<Board> findByIdAndIsDeletedFalse(Long id);

}