package com.ddg.achieveio.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record RegisterUserRequest(
        @NotEmpty(message = "Nome é obrigatório")
        String name,

        @NotEmpty(message = "E-mail é obrigatório")
        @Email(message = "Esse e-mail não é válido")
        String email,

        @NotEmpty(message = "Senha é obrigatória")
        String password
) {
}
