package com.aivle.bit.company.service;

import static com.aivle.bit.global.exception.ErrorCode.FILE_SIZE_EXCEEDED;
import static com.aivle.bit.global.exception.ErrorCode.FILE_UPLOAD_ERROR;
import static com.aivle.bit.global.exception.ErrorCode.INVALID_FILE_FORMAT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.global.utils.RandomGenerator;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.net.URL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class S3ServiceTest {

    @Mock
    private AmazonS3 s3Client;

    @Mock
    private RandomGenerator randomGenerator;

    @InjectMocks
    private S3Service s3Service;

    private MultipartFile file;

    @BeforeEach
    void setUp() {
        file = mock(MultipartFile.class);
    }

    @Test
    @DisplayName("[성공] 파일 업로드가 성공적으로 이루어진다.")
    void uploadImage_shouldUploadFileSuccessfully() throws IOException {
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getSize()).thenReturn(1024L * 1024L); // 1 MB
        when(file.getOriginalFilename()).thenReturn("test-image.jpg");
        when(randomGenerator.getRandomSHA256(anyString())).thenReturn("random-sha256");
        when(s3Client.putObject(any(PutObjectRequest.class))).thenReturn(null);

        String path = "test-path";
        String result = s3Service.uploadImage(file, path);

        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class));
        assertEquals("test-path/random-sha256.jpg", result);
    }

    @Test
    @DisplayName("[예외] 잘못된 파일 포맷으로 파일을 업로드하면 INVALID_FILE_FORMAT 예외가 발생한다.")
    void uploadImage_shouldThrowExceptionForInvalidFileFormat() {
        when(file.getContentType()).thenReturn("application/pdf");

        AivleException exception = assertThrows(AivleException.class, () -> {
            s3Service.uploadImage(file, "test-path");
        });

        assertEquals(INVALID_FILE_FORMAT, exception.getErrorCode());
    }

    @Test
    @DisplayName("[예외] 파일 사이즈가 5MB를 초과하면 FILE_SIZE_EXCEEDED 예외가 발생한다.")
    void uploadImage_shouldThrowExceptionForFileSizeExceeded() {
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getSize()).thenReturn(6L * 1024L * 1024L); // 6 MB

        AivleException exception = assertThrows(AivleException.class, () -> {
            s3Service.uploadImage(file, "test-path");
        });

        assertEquals(FILE_SIZE_EXCEEDED, exception.getErrorCode());
    }

    @Test
    @DisplayName("[예외] 파일 업로드 중 SdkClientException이 발생하면 FILE_UPLOAD_ERROR 예외가 발생한다.")
    void uploadImage_shouldThrowExceptionForSdkClientException() throws IOException {
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getSize()).thenReturn(1024L * 1024L); // 1 MB
        when(file.getOriginalFilename()).thenReturn("test-image.jpg");
        when(randomGenerator.getRandomSHA256(anyString())).thenReturn("random-sha256");
        when(s3Client.putObject(any(PutObjectRequest.class))).thenThrow(SdkClientException.class);

        AivleException exception = assertThrows(AivleException.class, () -> {
            s3Service.uploadImage(file, "test-path");
        });

        assertEquals(FILE_UPLOAD_ERROR, exception.getErrorCode());
    }

    @Test
    @DisplayName("[성공] 프리사인드 URL을 생성하면 유효한 URL이 반환된다.")
    void generatePresignedUrl_shouldReturnValidUrl() {
        String key = "test-key";
        URL url = mock(URL.class);
        when(url.toString()).thenReturn("http://presigned-url.com/test-key");
        when(s3Client.generatePresignedUrl(any(GeneratePresignedUrlRequest.class))).thenReturn(url);

        String result = s3Service.generatePresignedUrl(key);

        assertEquals("http://presigned-url.com/test-key", result);
    }
}
