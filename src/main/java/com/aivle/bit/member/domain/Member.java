package com.aivle.bit.member.domain;

import static com.aivle.bit.global.encode.PasswordEncoderSHA256.encode;
import static com.aivle.bit.global.exception.ErrorCode.*;

import com.aivle.bit.company.domain.Company;
import com.aivle.bit.global.domain.BaseTimeEntity;
import com.aivle.bit.global.exception.AivleException;
import jakarta.persistence.*;
import java.util.regex.Pattern;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
public class Member extends BaseTimeEntity {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{8,20}$";
    private static final String NAME_REGEX = "^[가-힣]{2,10}$";

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

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Comment("0-미인증, 1-회원 인증, 2-회원 휴면")
    private MemberState state;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @Comment("True-관리자, False-관리자 아님")
    private Boolean isAdmin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    protected Member() {
    }

    private Member(final String email, final String password, final String name, final String address,
                   final MemberState state, final Boolean isAdmin, final Company company) {
        this.email = email;
        this.password = encode(password);
        this.name = name;
        this.address = address;
        this.state = state;
        this.isAdmin = isAdmin;
        this.company = company;
    }

    public static Member of(final String email, final String password, final String name, final String address) {
        validate(email, password, name);
        return new Member(email, password, name, address, MemberState.UNVERIFIED, false, null);
    }

    private static void validate(final String email, final String password, final String name) {
        if (!isValidEmail(email)) {
            throw new AivleException(INVALID_EMAIL_FORMAT);
        }

        if (!isValidPassword(password)) {
            throw new AivleException(INVALID_PASSWORD_FORMAT);
        }

        if (!isValidName(name)) {
            throw new AivleException(INVALID_NAME_FORMAT);
        }
    }

    private static boolean isValidEmail(final String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    private static boolean isValidPassword(final String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
    }

    private static boolean isValidName(final String name) {
        return Pattern.matches(NAME_REGEX, name);
    }

    public void validatePassword(final String password) {
        if (!this.password.equals(encode(password))) {
            throw new AivleException(AUTH_INVALID_PASSWORD);
        }
    }
}