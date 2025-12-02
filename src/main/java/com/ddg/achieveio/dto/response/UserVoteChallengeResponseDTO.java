package com.ddg.achieveio.dto.response;

import com.ddg.achieveio.entity.VotesChallenges;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserVoteChallengeResponseDTO(
        UUID challengeId,
        String challengeName,
        LocalDateTime votedAt,
        Set<SimpleAchievementDTO> achievements
) {
    public static UserVoteChallengeResponseDTO of(VotesChallenges vote) {
        var challenge = vote.getChallenge();

        Set<SimpleAchievementDTO> achievementDTOs = challenge.getAchievements().stream()
                .map(SimpleAchievementDTO::new)
                .collect(Collectors.toSet());

        return new UserVoteChallengeResponseDTO(
                challenge.getId(),
                challenge.getChallengeName(),
                vote.getCreatedAt(),
                achievementDTOs
        );
    }
}