package com.ddg.achieveio.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL; // Importe este


public record PlatformRequestDTO(
        @NotBlank(message = "O nome da plataforma não pode estar em branco")
        String platformName,

        @NotBlank(message = "A descrição não pode estar em branco")
        String platformDescription,

        @URL(message = "A URL da imagem deve ser válida")
        String imageUrl
) {
}