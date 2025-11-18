package com.ddg.achieveio.dto.response;

import com.ddg.achieveio.entity.Challenge;

import java.util.UUID;
public record SimpleChallengeDTO(
        UUID id,
        String challengeName
) {
    public SimpleChallengeDTO(Challenge challenge) {
        this(
                challenge.getId(),
                challenge.getChallengeName()
        );
    }
}
