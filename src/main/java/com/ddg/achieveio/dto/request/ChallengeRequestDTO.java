package com.ddg.achieveio.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;


public record ChallengeRequestDTO(
        @NotBlank(message = "O nome do desafio não pode estar em branco")
        String challengeName,

        @NotBlank(message = "A descrição do desafio não pode estar em branco")
        String challengeDescription,

        @NotEmpty(message = "O desafio deve conter pelo menos um ID de conquista")
        Set<UUID> achievementIds
) {
}