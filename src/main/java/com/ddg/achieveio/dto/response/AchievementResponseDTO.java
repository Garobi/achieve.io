package com.ddg.achieveio.dto.response;

import com.ddg.achieveio.entity.Achievement;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
public record AchievementResponseDTO(
                UUID id,
                String achievementName,
                String achievementDescription,
                Boolean verified,
                SimpleAuthorDTO author,
                SimpleGameDTO game,
                Set<SimpleChallengeDTO> challenges
) {
    public AchievementResponseDTO(Achievement achievement) {
        this(
                achievement.getId(),
                achievement.getAchievementName(),
                achievement.getAchievementDescription(),
                achievement.getVerified(),
                (achievement.getAuthor() != null) ? new SimpleAuthorDTO(achievement.getAuthor()) : null,
                (achievement.getGame() != null) ? new SimpleGameDTO(achievement.getGame()) : null,
                achievement.getChallenges().stream()
                        .map(SimpleChallengeDTO::new)
                        .collect(Collectors.toSet())
        );
    }
}
