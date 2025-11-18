package com.ddg.achieveio.dto.response;

import com.ddg.achieveio.entity.Achievement;

import java.util.UUID;

record SimpleAchievementDTO(
        UUID id,
        String achievementName,
        String achievementDescription
) {

    public SimpleAchievementDTO(Achievement achievement) {
        this(
                achievement.getId(),
                achievement.getAchievementName(),
                achievement.getAchievementDescription()
        );
    }
}