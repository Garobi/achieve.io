package com.ddg.achieveio.dto.response;

import com.ddg.achieveio.entity.VotesAchievements;
import java.time.LocalDateTime;
import java.util.UUID;

public record AchievementVoteResponseDTO(
        UUID userId,
        String userName,
        String profilePicture,
        LocalDateTime votedAt
) {
    public static AchievementVoteResponseDTO of(VotesAchievements vote) {
        var user = vote.getUser();

        return new AchievementVoteResponseDTO(
                user.getId(),
                user.getName(),
                user.getProfilePicture(),
                vote.getCreatedAt()
        );
    }
}