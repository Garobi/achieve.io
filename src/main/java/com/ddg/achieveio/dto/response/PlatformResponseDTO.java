package com.ddg.achieveio.dto.response;

import com.ddg.achieveio.entity.Platform;
import java.util.UUID;

public record PlatformResponseDTO(
        UUID id,
        String platformName,
        String platformDescription,
        String imageUrl
) {

    public PlatformResponseDTO(Platform platform) {
        this(
                platform.getId(),
                platform.getPlatformName(),
                platform.getPlatformDescription(),
                platform.getImageUrl()
        );
    }
}