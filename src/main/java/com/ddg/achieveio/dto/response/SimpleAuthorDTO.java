package com.ddg.achieveio.dto.response;

import com.ddg.achieveio.entity.User;

import java.util.UUID;
public record SimpleAuthorDTO(
        UUID id,
        String name,
        String profilePicture
) {
    public SimpleAuthorDTO(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getProfilePicture()
        );
    }
}
