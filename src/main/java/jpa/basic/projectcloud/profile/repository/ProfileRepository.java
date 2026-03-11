package jpa.basic.projectcloud.profile.repository;

import jpa.basic.projectcloud.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("SELECT p.s3Key FROM Profile p WHERE p.user.id = :userId")
    Optional<String> findKeyByUserId(@Param("userId") Long userId);
}
