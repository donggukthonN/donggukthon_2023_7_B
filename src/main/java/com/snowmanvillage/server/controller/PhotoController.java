package com.snowmanvillage.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowmanvillage.server.dto.req.LocationRequestDto;
import com.snowmanvillage.server.dto.req.PhotoLikeRequestDto;
import com.snowmanvillage.server.dto.req.PhotoRequestDto;
import com.snowmanvillage.server.dto.resp.PhotoResponseDto;
import com.snowmanvillage.server.dto.req.PhotoUploadRequestDto;
import com.snowmanvillage.server.entity.Photo;
import com.snowmanvillage.server.repository.PhotoRepository;
import com.snowmanvillage.server.service.PasswordBCryptService;
import com.snowmanvillage.server.service.PhotoService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("/photo")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;
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
            String photoRequest = httpServletRequest.getParameter("photoRequest");
            String locationRequest = httpServletRequest.getParameter("locationRequest");
            PhotoUploadRequestDto requestDto = objectMapper.readValue(photoRequest, PhotoUploadRequestDto.class);
            LocationRequestDto locationRequestDto = objectMapper.readValue(locationRequest, LocationRequestDto.class);
            String encodedPassword = passwordBCryptService.encodePassword(requestDto.getPassword());

            Photo savedPhoto = photoService.uploadPhoto(image, requestDto, locationRequestDto);
            savedPhoto.setPassword(encodedPassword);
            photoRepository.save(savedPhoto);
            return ResponseEntity.ok("포토 등록 완료");
        } catch (Exception e) {
            return ResponseEntity.ok("포토 등록 실패");
        }
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoResponseDto> getPhoto(@PathVariable(name = "photoId") Long photoId) {
        return ResponseEntity.ok(photoService.getPhotoByPhotoId(photoId));
    }

    @GetMapping("/list")
    public ResponseEntity<List<PhotoResponseDto>> getPhotoList(@RequestParam(name = "orderBy") String orderBy) {
        return ResponseEntity.ok(photoService.getPhotoList(orderBy));
    }

    @PostMapping("/delete/{photoId}")
    public ResponseEntity<String> deletePhoto(@PathVariable(name = "photoId") Long photoId, @RequestBody PhotoRequestDto requestDto) {
        try {
            return ResponseEntity.ok(photoService.deletePhoto(photoId, requestDto.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.ok("포토 삭제 실패");
        }
    }

    @PutMapping("/like")
    public ResponseEntity<PhotoResponseDto> likePhoto(@RequestBody PhotoLikeRequestDto requestDto) {
        return ResponseEntity.ok(photoService.likePhoto(requestDto.getPhoto_id()));
    }

    @PutMapping("/unlike")
    public ResponseEntity<PhotoResponseDto> unlikePhoto(@RequestBody PhotoLikeRequestDto requestDto) {
        return ResponseEntity.ok(photoService.unlikePhoto(requestDto.getPhoto_id()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PhotoResponseDto>> searchPhoto(@RequestParam(name = "searchType") String searchType, @RequestParam(name = "searchValue") String searchValue) {
        return ResponseEntity.ok(photoService.searchPhoto(searchType, searchValue));
    }
}
