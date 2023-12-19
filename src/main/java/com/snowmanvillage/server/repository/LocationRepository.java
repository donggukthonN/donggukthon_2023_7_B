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

    @Query("SELECT l.photo FROM Location l "
        + "WHERE l.address LIKE %:searchValue% ")
    List<Photo> findByLocationContaining(@Param("searchValue") String searchValue);
}
