package com.ddg.achieveio.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AchievementRequest(
        @NotBlank(message = "O nome não pode estar em branco")
        String achievement_name,

        @NotBlank(message = "A descrição não pode estar em branco")
        String achievement_description,

        @NotNull(message = "O ID do jogo não pode ser nulo")
        UUID game_id // Assumindo que o ID do Game também é UUID
) {
}
