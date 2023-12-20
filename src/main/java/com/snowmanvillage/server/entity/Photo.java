package com.snowmanvillage.server.entity;

import com.snowmanvillage.server.entity.global.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class Photo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_path")
    private String filePath;

    private String title;

    private String username;

    @Setter
    private String password;

    @Setter
    @Column(name = "like_count")
    private Integer likeCount;

    @Builder
    public Photo(String filePath, String title, String username, String password) {
        this.filePath = filePath;
        this.title = title;
        this.username = username;
        this.password = password;
        this.likeCount = 0;
    }
}
