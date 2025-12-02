package com.ddg.achieveio.dto.response;

import com.ddg.achieveio.entity.Game;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
public record SimpleGameDTO(
                UUID id,
                String gameName,
                String imageUrl,
                Set<SimplePlatformDTO> platforms
) {
    public SimpleGameDTO(Game game) {
        this(
                game.getId(),
                game.getGameName(),
                game.getImageUrl(),
                game.getPlatforms().stream()
                        .map(SimplePlatformDTO::new)
                        .collect(Collectors.toSet())
        );
    }
}
