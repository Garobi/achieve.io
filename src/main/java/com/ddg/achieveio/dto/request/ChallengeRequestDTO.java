package com.ddg.achieveio.dto.request;

import com.ddg.achieveio.validator.NotEmptyUUIDList;
import com.ddg.achieveio.validator.ValidResourceName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;


public record ChallengeRequestDTO(
        @ValidResourceName(min = 5, max = 50, message = "O nome do desafio deve ter entre 5 e 50 caracteres.")
        String challengeName,

        @NotBlank(message = "A descrição do desafio não pode estar em branco")
        String challengeDescription,

        @NotEmptyUUIDList(message = "O desafio deve incluir pelo menos uma conquista.")
        Set<UUID> achievementIds
) {
}