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
        uniqueVisitors.putIfAbsent(sessionId, today);
        if (!uniqueVisitors.get(sessionId).equals(today)) {
            uniqueVisitors.put(sessionId, today);
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
