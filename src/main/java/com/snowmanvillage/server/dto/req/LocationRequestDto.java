package com.snowmanvillage.server.dto.req;

import com.snowmanvillage.server.entity.Location;
import com.snowmanvillage.server.entity.Photo;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LocationRequestDto {
    private String roadName;
    private String lotNumber;
    private Double latitude;
    private Double longitude;

    public Location toEntity(LocationRequestDto requestDto, Photo photo) {
        return Location.builder()
                .photo(photo)
                .roadName(requestDto.getRoadName())
                .lotNumber(requestDto.getLotNumber())
                .latitude(requestDto.getLatitude())
                .longitude(requestDto.getLongitude())
                .build();
    }
}
