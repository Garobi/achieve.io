package com.ddg.achieveio.dto.request;

import com.ddg.achieveio.validator.ValidResourceName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AchievementRequest(
        @ValidResourceName(min = 5, max = 50, message = "O nome da conquista deve ter entre 5 e 50 caracteres.")
        String achievement_name,

        @NotBlank(message = "A descrição não pode estar em branco")
        String achievement_description,

        @NotNull(message = "O ID do jogo não pode ser nulo")
        UUID game_id
) {
}
