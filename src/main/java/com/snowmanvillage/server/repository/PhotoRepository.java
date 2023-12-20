package com.snowmanvillage.server.repository;

import com.snowmanvillage.server.entity.Photo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByTitleContaining(String searchValue);

    List<Photo> findByUsernameContaining(String searchValue);
}
