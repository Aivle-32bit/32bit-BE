package com.aivle.bit.auth.logging.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("로그인 기록의 고유 식별자")
    private Long id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("로그인 시각")
    private Date loginTime;

    @Column(nullable = false)
    @Comment("로그인 시도한 IP 주소")
    private String ipAddress;

    @Column
    @Comment("로그인 시도한 사용자의 브라우저 정보")
    private String userAgent;

    @Column
    @Comment("로그인 시도한 사용자 ID")
    private Long userId;

    @Column(nullable = false)
    @Comment("로그인 성공 여부")
    private boolean success;

    private LoginRecord(String ipAddress, String userAgent, Long userId, boolean success) {
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.userId = userId;
        this.success = success;
    }

    // 정적 팩토리 메서드
    public static LoginRecord of(String ipAddress, String userAgent, Long userId, boolean success) {
        return new LoginRecord(ipAddress, userAgent, userId, success);
    }
}
