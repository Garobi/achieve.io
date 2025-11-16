package com.ddg.achieveio.dto.response;

import com.ddg.achieveio.entity.Game;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record GameResponse(
        UUID id,
        String gameName,
        String gameDescription,
        String imageUrl,
        Set<PlatformSummary> platforms,
        Integer achievementsCount
) {
    public static GameResponse fromEntity(Game game) {
        Set<PlatformSummary> platforms = game.getPlatforms() != null
                ? game.getPlatforms().stream()
                        .map(p -> new PlatformSummary(p.getId(), p.getPlatformName()))
                        .collect(Collectors.toSet())
                : Set.of();

        Integer achievementsCount = game.getAchievements() != null
                ? game.getAchievements().size()
                : 0;

        return new GameResponse(
                game.getId(),
                game.getGameName(),
                game.getGameDescription(),
                game.getImageUrl(),
                platforms,
                achievementsCount
        );
    }

    public record PlatformSummary(UUID id, String name) {
    }
}
