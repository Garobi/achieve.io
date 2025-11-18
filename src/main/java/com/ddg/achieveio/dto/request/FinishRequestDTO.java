package com.ddg.achieveio.dto.request;

import com.ddg.achieveio.entity.AchievementsFinished;
import com.ddg.achieveio.entity.Game; // Importe o Game
import com.ddg.achieveio.entity.User; // Importe o User
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;


public record FinishRequestDTO(
        @NotNull(message = "O ID da conquista é obrigatório")
        UUID achievementId
) {
}


