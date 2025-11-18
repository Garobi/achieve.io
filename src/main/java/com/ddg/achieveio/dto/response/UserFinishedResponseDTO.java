package com.ddg.achieveio.dto.response;

import com.ddg.achieveio.entity.AchievementsFinished;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserFinishedResponseDTO(
        UUID userId,
        String userName,
        String profilePicture,
        LocalDateTime completedAt
) {
    public UserFinishedResponseDTO(AchievementsFinished finished) {

        this(
                finished.getUser().getId(),
                finished.getUser().getName(), // Usando o 'name' do User.java
                finished.getUser().getProfilePicture(),
                finished.getCompletedAt()
        );
    }
}
