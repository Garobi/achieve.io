package com.ddg.achieveio.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterAchievementRequest(
        @NotBlank(message = "Nome da conquista é obrigatório")
        String achievement_name,

        @NotBlank(message = "Descrição da conquista é obrigatório")
        String achievement_description,

        @NotNull(message = "Jogo relacionado a conquista é obrigatório")
        Long game_id
) {
}
