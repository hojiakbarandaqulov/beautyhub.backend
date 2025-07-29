package com.example.repository;

import com.example.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    @Query("""
       select t from TokenEntity t inner join  ProfileEntity p
       on t.profile.id = p.id
       where t.profile.id=?1 and t.loggedOut=false""")
    List<TokenEntity> findAllTokenByProfile(Long profileId);

    Optional<TokenEntity> findByToken(String token);


}
