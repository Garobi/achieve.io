package com.ddg.achieveio.repository;

import com.ddg.achieveio.entity.Achievement;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AchievementRepository extends JpaRepository<Achievement, UUID> {

    @EntityGraph(
            attributePaths = {
                    "author",
                    "game",
                    "challenges",
                    "game.platforms"
            }
    )
    @Query("SELECT a FROM Achievement a")
    List<Achievement> findAllWithDetails();
}