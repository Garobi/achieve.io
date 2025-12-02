package com.ddg.achieveio.repository;

import com.ddg.achieveio.entity.VotesAchievements;
import com.ddg.achieveio.entity.VotesAchievementsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VotesAchievementsRepository extends JpaRepository<VotesAchievements, VotesAchievementsId> {

    List<VotesAchievements> findByUserId(UUID userId);

    List<VotesAchievements> findByAchievementId(UUID achievementId);

    long countByAchievementId(UUID achievementId);
}