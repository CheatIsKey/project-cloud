package jpa.basic.projectcloud.profile.controller;

import jpa.basic.projectcloud.exception.ApiResponse;
import jpa.basic.projectcloud.profile.dto.response.FileDownloadUrlResponse;
import jpa.basic.projectcloud.profile.dto.response.FileUploadResponse;
import jpa.basic.projectcloud.profile.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class ProfileController {

    private final S3Service s3Service;

    @PostMapping("/{userId}/profile-image")
    public ResponseEntity<ApiResponse<FileUploadResponse>> upload(
            @PathVariable Long userId, @RequestParam("file") MultipartFile file) {

        String key = s3Service.upload(userId, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, new FileUploadResponse(key)));
    }

    @GetMapping("/{userId}/profile-image")
    public ResponseEntity<ApiResponse<FileDownloadUrlResponse>> download(
            @PathVariable Long userId) {
        String url = s3Service.getDownloadUrl(userId);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, new FileDownloadUrlResponse(url)));
    }
}
