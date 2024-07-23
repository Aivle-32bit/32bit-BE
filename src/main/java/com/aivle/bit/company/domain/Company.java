package com.aivle.bit.company.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("회사명")
    @Column(nullable = false, unique = true)
    private String name;

    @Comment("사업분야")
    @Column(nullable = false)
    private String businessType;

    @Comment("회사 이미지 URL")
    @Column
    private String imageUrl;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @Comment("True-삭제, False-삭제 아님")
    private boolean isDeleted;


    private Company(String name, String businessType, String imageUrl) {
        this.name = name;
        this.businessType = businessType;
        this.imageUrl = imageUrl;
        this.isDeleted = false;
    }

    public static Company of(String name, String businessType, String imageUrl) {
        return new Company(name, businessType, imageUrl);
    }

    public void delete() {
        this.isDeleted = true;
    }

}