package com.ddg.achieveio.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotEmpty(message = "Email não pode ser vazio") String email,
        @NotEmpty(message = "Senha não pode ser vazia") String password) {
}
