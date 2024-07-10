package com.aivle.bit.board.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private Long memberId;
    private int state;   // 삭제 여부
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToOne
    private BoardDetails boardDetails;

    @Builder
    public Board(Long id, String title, String content, Long memberId, int state,
                 LocalDateTime createdAt, LocalDateTime updatedAt, BoardDetails boardDetails) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.boardDetails = boardDetails;
    }

    public static Board of(String title, String content, Long memberId) {
        return Board.builder()
            .title(title)
            .content(content)
            .memberId(memberId)
            .state(0)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .boardDetails(BoardDetails.builder().secret(false).boardpw(null).build())
            .build();
    }
}