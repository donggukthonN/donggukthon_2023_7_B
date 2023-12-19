package com.snowmanvillage.server.service;

import com.snowmanvillage.server.dto.PhotoResponseDto;
import com.snowmanvillage.server.dto.PhotoUploadRequestDto;
import com.snowmanvillage.server.entity.Photo;
import com.snowmanvillage.server.entity.global.BaseTimeEntity;
import com.snowmanvillage.server.repository.PhotoRepository;
import jakarta.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final PasswordBCryptService passwordBCryptService;

    @Autowired
    private S3Uploader s3Uploader;

    public Photo uploadPhoto(MultipartFile image, PhotoUploadRequestDto requestDto) {
        String photoUrl = s3Uploader.upload(image, "images");
        return photoRepository.save(requestDto.toEntity(photoUrl, requestDto));
    }

    public PhotoResponseDto getPhotoByPhotoId(Long id) {
        Photo photo = photoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 포토가 없습니다."));
        return PhotoResponseDto.of(photo);
    }

    public List<PhotoResponseDto> getPhotoList(String orderBy) {
        // orderBy : LIKES, DATE(최신순)
        List<Photo> photoList = photoRepository.findAll();
        switch (orderBy) {
            case "LIKES":
                photoList.sort((o1, o2) -> o2.getLikeCount().compareTo(o1.getLikeCount()));
                break;
            case "DATE":
                photoList.sort(Comparator.comparing(BaseTimeEntity::getCreatedDate));
                break;
            default:
                throw new IllegalArgumentException("orderBy 값이 잘못되었습니다.");
        }
        return PhotoResponseDto.listOf(photoList);
    }

    @Transactional
    public String deletePhoto(Long photoId, String password) {
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new IllegalArgumentException("해당 포토가 없습니다."));
        log.info("password: {}, photo get password : {} ", password, photo.getPassword());
        if (passwordBCryptService.matchPassword(password, photo.getPassword())) {
            photoRepository.delete(photo);
            return "포토 삭제 완료";
        } else {
            return "비밀번호가 틀렸습니다.";
        }
    }

    @Transactional
    public PhotoResponseDto likePhoto(Long photoId) {
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new IllegalArgumentException("해당 포토가 없습니다."));
        photo.setLikeCount(photo.getLikeCount() + 1);
        return PhotoResponseDto.of(photo);
    }

    @Transactional
    public PhotoResponseDto unlikePhoto(Long photoId) {
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new IllegalArgumentException("해당 포토가 없습니다."));
        photo.setLikeCount(photo.getLikeCount() - 1);
        return PhotoResponseDto.of(photo);
    }
}
