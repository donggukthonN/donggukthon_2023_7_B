package com.snowmanvillage.server.entity;

import com.snowmanvillage.server.entity.global.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Location extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="photo_id")
    private Photo photo;

    private String roadName;

    private String lotNumber;

    private Double latitude;

    private Double longitude;

    @Builder
    public Location(String roadName, String lotNumber, Double latitude, Double longitude) {
        this.roadName = roadName;
        this.lotNumber = lotNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
