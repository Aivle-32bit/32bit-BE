package com.aivle.bit.company.service;

import static com.aivle.bit.global.exception.ErrorCode.FILE_DELETE_ERROR;
import static com.aivle.bit.global.exception.ErrorCode.FILE_SIZE_EXCEEDED;
import static com.aivle.bit.global.exception.ErrorCode.FILE_UPLOAD_ERROR;
import static com.aivle.bit.global.exception.ErrorCode.INVALID_FILE_FORMAT;

import com.aivle.bit.global.exception.AivleException;
import com.aivle.bit.global.utils.RandomGenerator;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3Client;
    private final RandomGenerator randomGenerator;

    @Value("${s3.credentials.bucket}")
    private String bucket;

    // 허용되는 이미지 파일 포맷
    private static final List<String> ALLOWED_MIME_TYPES = List.of(
        "image/jpeg",
        "image/png",
        "image/webp",
        "image/bmp",
        "image/svg+xml",
        "image/tiff",
        "image/vnd.microsoft.icon",
        "image/heic",
        "image/heif"
    );

    @Retryable(value = {SdkClientException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public String uploadImage(final MultipartFile file, final String path) {
        validateFileType(file);

        String fileName = randomGenerator.getRandomSHA256(file.getOriginalFilename());
        final String key = String.format("%s/%s.%s", path, fileName,
            FilenameUtils.getExtension(file.getOriginalFilename()));

        try {
            requestToUpload(file, key);
        } catch (SdkClientException | IOException e) {
            log.error("Failed to upload file: {}", file.getOriginalFilename(), e);
            throw new AivleException(FILE_UPLOAD_ERROR);
        }

        return key;
    }

    private void validateFileType(MultipartFile file) {
        String mimeType = file.getContentType();
        if (!ALLOWED_MIME_TYPES.contains(mimeType)) {
            throw new AivleException(INVALID_FILE_FORMAT);
        }

        long fileSizeInMB = file.getSize() / (1024 * 1024);
        if (fileSizeInMB > 5) {
            throw new AivleException(FILE_SIZE_EXCEEDED);
        }
    }

    private void requestToUpload(final MultipartFile file, final String key) throws IOException {
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        s3Client.putObject(new PutObjectRequest(bucket, key, file.getInputStream(), metadata));
    }

    public String generatePresignedUrl(String key) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += TimeUnit.HOURS.toMillis(1); // 1시간 유효
        expiration.setTime(expTimeMillis);

        // 프리사인드 URL 생성 요청
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
            new GeneratePresignedUrlRequest(bucket, key)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        // 프리사인드 URL 생성
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();
    }

    public void deleteImage(String key) {
        if (key == null) {
            return;
        }
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucket, key));
            log.info("Successfully deleted image with key: {}", key);
        } catch (SdkClientException e) {
            log.error("Failed to delete image with key: {}", key, e);
            throw new AivleException(FILE_DELETE_ERROR);
        }
    }
}
