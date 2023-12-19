package com.snowmanvillage.server.repository;

import com.snowmanvillage.server.entity.Location;
import com.snowmanvillage.server.entity.Photo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    // roadName, lotNumber 에 searchValue 가 포함되어 있는 Location 을 반환
    @Query("SELECT l.photo FROM Location l "
        + "WHERE l.roadName LIKE %:searchValue% "
        + "OR l.lotNumber LIKE %:searchValue%")
    List<Photo> findByLocationContaining(@Param("searchValue") String searchValue);
}
