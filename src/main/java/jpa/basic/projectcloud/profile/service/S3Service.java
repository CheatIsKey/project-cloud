package jpa.basic.projectcloud.profile.service;

import io.awspring.cloud.s3.S3Template;
import jpa.basic.projectcloud.exception.CustomException;
import jpa.basic.projectcloud.exception.ErrorCode;
import jpa.basic.projectcloud.profile.entity.Profile;
import jpa.basic.projectcloud.profile.repository.ProfileRepository;
import jpa.basic.projectcloud.user.entity.User;
import jpa.basic.projectcloud.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class S3Service {

    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofDays(7);
    private final S3Template s3Template;
    private final UserService userService;
    private final ProfileRepository profileRepository;

    @Value("${S3_BUCKET_NAME}")
    private String bucket;

    @Transactional
    public String upload(Long userId, MultipartFile file) {
        User user = userService.findUserById(userId);

        try {
            String filename = file.getOriginalFilename();
            String key = "uploads/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

            s3Template.upload(bucket, key, file.getInputStream());

            Profile profile = new Profile(user, key, filename);
            profileRepository.save(profile);

        return key;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_UPLOAD_FAIL);
        }
    }

    public URL getDownloadUrl(Long userId) {
        String key = profileRepository.findKeyByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.PROFILE_NOT_FOUND));

        return s3Template.createSignedGetURL(bucket, key, PRESIGNED_URL_EXPIRATION);
    }
}
