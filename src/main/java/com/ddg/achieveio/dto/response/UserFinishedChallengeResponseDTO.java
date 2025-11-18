package com.ddg.achieveio.dto.response;

import com.ddg.achieveio.entity.ChallengesFinished;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserFinishedChallengeResponseDTO(
        UUID userId,
        String userName,
        String profilePicture,
        LocalDateTime completedAt
) {

    public static UserFinishedChallengeResponseDTO of(ChallengesFinished finished) {
        var user = finished.getUser();

        return new UserFinishedChallengeResponseDTO(
                user.getId(),
                user.getName(),
                user.getProfilePicture(),
                finished.getCompletedAt()
        );
    }
}