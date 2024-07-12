package com.aivle.bit.auth.logging.service;

import com.aivle.bit.auth.logging.domain.VisitorCount;
import com.aivle.bit.auth.logging.repository.VisitorCountRepository;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitorService {

    private final VisitorCountRepository visitorCountRepository;
    private final ConcurrentHashMap<String, LocalDate> uniqueVisitors = new ConcurrentHashMap<>();

    public void recordVisit(String sessionId) {
        LocalDate today = LocalDate.now();

        // 기존 방문 기록이 없거나, 날짜가 오늘과 다를 경우에만 카운트 증가
        if (uniqueVisitors.put(sessionId, today) == null || !uniqueVisitors.get(sessionId).equals(today)) {
            incrementVisitCount(today);
        }
    }

    private synchronized void incrementVisitCount(LocalDate date) {
        VisitorCount visitorCount = visitorCountRepository.findByVisitDate(date)
            .orElseGet(() -> VisitorCount.of(date, 0L));
        visitorCount.incrementVisitCount();
        visitorCountRepository.save(visitorCount);
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void clearVisitorCache() {
        uniqueVisitors.clear();
    }
}