package com.snowmanvillage.server.service;

import com.snowmanvillage.server.dto.PhotoUploadRequestDto;
import com.snowmanvillage.server.entity.Photo;
import com.snowmanvillage.server.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PhotoUploadService {

    private final PhotoRepository photoRepository;

    @Autowired
    private S3Uploader s3Uploader;

    public Photo uploadPhoto(MultipartFile image, PhotoUploadRequestDto requestDto) {
        String photoUrl = s3Uploader.upload(image, "images");
        return photoRepository.save(requestDto.toEntity(photoUrl, requestDto));
    }

}
