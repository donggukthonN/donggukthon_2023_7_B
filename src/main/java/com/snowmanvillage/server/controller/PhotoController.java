package com.snowmanvillage.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowmanvillage.server.dto.PhotoUploadRequestDto;
import com.snowmanvillage.server.entity.Photo;
import com.snowmanvillage.server.repository.PhotoRepository;
import com.snowmanvillage.server.service.PasswordBCryptService;
import com.snowmanvillage.server.service.PhotoUploadService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("/photo")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoUploadService photoUploadService;
    private final PasswordBCryptService passwordBCryptService;
    private final ObjectMapper objectMapper;
    private final PhotoRepository photoRepository;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPhoto(HttpServletRequest httpServletRequest) {
        if (!(httpServletRequest instanceof MultipartHttpServletRequest)) {
            throw new IllegalArgumentException("MultipartHttpServletRequest is not valid");
        }
        try {
            MultipartFile image = ((MultipartHttpServletRequest) httpServletRequest).getFile("image");
            String request = httpServletRequest.getParameter("request");
            PhotoUploadRequestDto requestDto = objectMapper.readValue(request, PhotoUploadRequestDto.class);
            String encodedPassword = passwordBCryptService.encodePassword(requestDto.getPassword());
            Photo savedPhoto = photoUploadService.uploadPhoto(image, requestDto);
            savedPhoto.setPassword(encodedPassword);
            photoRepository.save(savedPhoto);
            return ResponseEntity.ok("포토 등록 완료");
        } catch (Exception e) {
            return ResponseEntity.ok("포토 등록 실패");
        }
    }

}
