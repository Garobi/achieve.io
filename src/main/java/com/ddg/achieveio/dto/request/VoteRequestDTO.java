package com.ddg.achieveio.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record VoteRequestDTO(
        @NotNull(message = "O ID da conquista é obrigatório")
        UUID achievementId
) {
}