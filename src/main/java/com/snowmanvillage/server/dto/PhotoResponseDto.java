package com.snowmanvillage.server.dto;

import com.snowmanvillage.server.entity.Photo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoResponseDto {
    private Long id;
    private String imageUrl;
    private String title;
    private String username;
    private Integer likeCount;

    @Builder
    public static PhotoResponseDto of(Photo photo) {
        return PhotoResponseDto.builder()
            .id(photo.getId())
            .imageUrl(photo.getFilePath())
            .title(photo.getTitle())
            .username(photo.getUsername())
            .likeCount(photo.getLikeCount())
            .build();
    }

    public static List<PhotoResponseDto> listOf(List<Photo> photoList) {
        return photoList.stream()
            .map(PhotoResponseDto::of)
            .toList();
    }
}
