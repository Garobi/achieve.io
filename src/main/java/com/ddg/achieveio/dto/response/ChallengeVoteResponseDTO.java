package com.ddg.achieveio.dto.response;

import com.ddg.achieveio.entity.VotesChallenges;
import java.time.LocalDateTime;
import java.util.UUID;

public record ChallengeVoteResponseDTO(
        UUID userId,
        String userName,
        String profilePicture,
        LocalDateTime votedAt
) {
    public static ChallengeVoteResponseDTO of(VotesChallenges vote) {
        var user = vote.getUser();

        return new ChallengeVoteResponseDTO(
                user.getId(),
                user.getName(),
                user.getProfilePicture(),
                vote.getCreatedAt()
        );
    }
}