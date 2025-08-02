package com.example.repository;

import com.example.entity.CityEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<CityEntity,Long> {



    @Query("SELECT c FROM CityEntity c WHERE " +
            "LOWER(c.nameUz) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.nameRu) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.nameEn) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<CityEntity> fullTextSearch(@Param("query") String query);

    @Query("from CityEntity where id=?1")
    CityEntity byId(Long id);



  /*  @Transactional
    @Modifying
    @Query("update CityEntity set id='null' where =?1")
    CityEntity deleteCityId(Long profileId);*/
}
