package com.aivle.bit.member.domain;

import com.aivle.bit.company.domain.Company;
import com.aivle.bit.global.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 64)
    private String email;

    @Column(nullable = false, columnDefinition = "CHAR(64)")
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Comment("0-미인증, 1-일반 회원 인증, 2-관리자 인증, 3-일반 회원 휴먼, 4-관리자 휴먼")
    private Short state;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @Comment("True-관리자, False-관리자 아님")
    private Boolean isAdmin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    protected Member() {
    }
}