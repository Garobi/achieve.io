package com.ddg.achieveio.dto.response;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record AchievementResponse(
        UUID id,
        String achievementName,
        String achievementDescription,
        Boolean verified,
        SimpleAuthorDTO author,
        SimpleGameDTO game,
        Set<SimpleChallengeDTO> challenges
) {
}
