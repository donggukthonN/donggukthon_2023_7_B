package com.snowmanvillage.server.dto;

import com.snowmanvillage.server.entity.Photo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoUploadRequestDto {

    private String title;
    private String username;
    private String password;

    public Photo toEntity(String photoUrl, PhotoUploadRequestDto requestDto) {
        return Photo.builder()
                .title(requestDto.getTitle())
                .username(requestDto.getUsername())
                .password(requestDto.getPassword())
                .filePath(photoUrl)
                .build();
    }
}
