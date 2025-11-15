package com.ddg.achieveio.dto.request;

import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

public record UpdateGameRequest(
        @Size(min = 2, max = 255, message = "Nome deve ter entre 2 e 255 caracteres")
        String gameName,

        @Size(max = 1000, message = "Descrição não pode exceder 1000 caracteres")
        String gameDescription,

        @Size(max = 500, message = "URL da imagem não pode exceder 500 caracteres")
        String imageUrl,

        Set<UUID> platformIds
) {
}
