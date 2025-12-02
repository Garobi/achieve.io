package com.ddg.achieveio.dto.response;

import com.ddg.achieveio.entity.AchievementsFinished;

import java.time.LocalDateTime;
import java.util.UUID;

public record AchievementsFinishedResponseDTO(
        UUID achievementId,
        String achievementName,
        String achievementDescription,
        SimpleGameDTO game,
        LocalDateTime completedAt
) {

    public AchievementsFinishedResponseDTO(AchievementsFinished finished) {
        this(
                finished.getAchievement().getId(),
                finished.getAchievement().getAchievementName(),
                finished.getAchievement().getAchievementDescription(),
                (finished.getAchievement().getGame() != null)
                        ? new SimpleGameDTO(finished.getAchievement().getGame())
                        : null,
                finished.getCompletedAt()
        );
    }
}