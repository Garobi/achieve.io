package com.ddg.achieveio.repository;

import com.ddg.achieveio.entity.AchievementsFinished;
import com.ddg.achieveio.entity.AchievementsFinishedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AchievementsFinishedRepository extends JpaRepository<AchievementsFinished, AchievementsFinishedId> {

    List<AchievementsFinished> findByUserId(UUID userId);

    List<AchievementsFinished> findByAchievementId(UUID achievementId);
}