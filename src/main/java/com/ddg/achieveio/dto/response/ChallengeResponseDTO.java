package com.ddg.achieveio.dto.response;

import com.ddg.achieveio.entity.Challenge;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record ChallengeResponseDTO(
        UUID id,
        String challengeName,
        String challengeDescription,
        Boolean verified,
        SimpleAuthorDTO author,
        Set<SimpleAchievementDTO> achievements
) {

    public ChallengeResponseDTO(Challenge challenge) {
        this(
                challenge.getId(),
                challenge.getChallengeName(),
                challenge.getChallengeDescription(),
                challenge.getVerified(),
                (challenge.getChallengeAuthor() != null)
                        ? new SimpleAuthorDTO(challenge.getChallengeAuthor())
                        : null,
                challenge.getAchievements().stream()
                        .map(SimpleAchievementDTO::new)
                        .collect(Collectors.toSet())
        );
    }
}

