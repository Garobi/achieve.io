package com.ddg.achieveio.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;


public record FinishRequestDTO(
        @NotNull(message = "O ID da conquista é obrigatório")
        UUID achievementId
) {
}


