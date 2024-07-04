package com.aivle.bit.global.encode;

import static com.aivle.bit.global.exception.ErrorCode.PASSWORD_ENCRYPTION_ERROR;

import com.aivle.bit.global.exception.AivleException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PasswordEncoderSHA256 {

    private static final String ALGORITHM = "SHA-256";

    public static String encode(CharSequence rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] byteData = digest.digest(rawPassword.toString().getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : byteData) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("Don't support algorithm: {}", ALGORITHM);
            throw new AivleException(PASSWORD_ENCRYPTION_ERROR);
        }
    }

    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}