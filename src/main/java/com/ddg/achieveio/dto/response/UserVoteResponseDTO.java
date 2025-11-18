package com.ddg.achieveio.dto.response;

import com.ddg.achieveio.entity.VotesAchievements;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserVoteResponseDTO(
        UUID achievementId,
        String achievementName,
        SimpleGameDTO game, // Reutilizando DTO
        LocalDateTime votedAt
) {
    public static UserVoteResponseDTO of(VotesAchievements vote) {
        var achievement = vote.getAchievement();

        return new UserVoteResponseDTO(
                achievement.getId(),
                achievement.getAchievementName(),
                (achievement.getGame() != null)
                        ? new SimpleGameDTO(achievement.getGame())
                        : null,
                vote.getCreatedAt()
        );
    }
}