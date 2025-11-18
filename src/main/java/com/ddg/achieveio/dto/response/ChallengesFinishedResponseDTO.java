package com.ddg.achieveio.dto.response;

import com.ddg.achieveio.entity.Achievement;
import com.ddg.achieveio.entity.ChallengesFinished;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


public record ChallengesFinishedResponseDTO(
        UUID challengeId,
        String challengeName,
        String challengeDescription,
        LocalDateTime completedAt,
        Set<SimpleAchievementDTO> achievements
) {

    public static ChallengesFinishedResponseDTO of(ChallengesFinished finished) {
        var challenge = finished.getChallenge();

        Set<SimpleAchievementDTO> achievementDTOs = challenge.getAchievements().stream()
                .map(SimpleAchievementDTO::new)
                .collect(Collectors.toSet());

        return new ChallengesFinishedResponseDTO(
                challenge.getId(),
                challenge.getChallengeName(),
                challenge.getChallengeDescription(),
                finished.getCompletedAt(),
                achievementDTOs
        );
    }
}
