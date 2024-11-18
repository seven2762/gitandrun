package com.sparta.gitandrun.googleAi.repository;

import com.sparta.gitandrun.googleAi.entity.Ai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface AiRepository extends JpaRepository<Ai, Long> {

    @Modifying
    @Transactional
    @Query("Delete from Ai a where a.createdAt < :oldDate")
    void deleteOlder(LocalDateTime oldDate);
}
