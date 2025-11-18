package com.ddg.achieveio.dto.response;

import com.ddg.achieveio.entity.Platform;

import java.util.UUID;
public record SimplePlatformDTO(
        UUID id,
        String name
) {
    public SimplePlatformDTO(Platform platform) {
        this(
                platform.getId(),
                platform.getPlatformName()
        );
    }
}
