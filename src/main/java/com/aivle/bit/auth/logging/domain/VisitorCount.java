package com.aivle.bit.auth.logging.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "visitor_count")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class VisitorCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Comment("방문 날짜")
    private LocalDate visitDate;

    @Column
    @Comment("방문 수")
    private Long visitCount;

    private VisitorCount(LocalDate visitDate, Long visitCount) {
        this.visitDate = visitDate;
        this.visitCount = visitCount;
    }

    public static VisitorCount of(LocalDate visitDate, Long visitCount) {
        return new VisitorCount(visitDate, visitCount);
    }

    public void incrementVisitCount() {
        this.visitCount++;
    }
}
