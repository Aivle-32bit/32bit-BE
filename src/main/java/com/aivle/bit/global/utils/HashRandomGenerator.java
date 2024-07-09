package com.aivle.bit.global.utils;

import static com.aivle.bit.global.exception.ErrorCode.SHA256AlgorithmNotFoundException;
import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_256;

import com.aivle.bit.global.exception.AivleException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Component;

@Component
public class HashRandomGenerator implements RandomGenerator {

    @Override
    public String getRandomSHA256(final String message) {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA_256);
            byte[] encodedhash = digest.digest(
                message.getBytes(StandardCharsets.UTF_8));

            return byteArrayToString(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new AivleException(SHA256AlgorithmNotFoundException);
        }
    }

    private String byteArrayToString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02x", b));
        }

        return stringBuilder.toString();
    }
}
