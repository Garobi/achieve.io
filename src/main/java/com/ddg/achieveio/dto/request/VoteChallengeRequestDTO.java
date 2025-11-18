package com.ddg.achieveio.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record VoteChallengeRequestDTO(
        @NotNull(message = "O ID do desafio é obrigatório")
        UUID challengeId
) {
}