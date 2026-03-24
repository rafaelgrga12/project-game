package com.example.game.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Usuário retornado pela API (senha não é exposta)")
public record UsuarioResponse(
		@Schema(example = "1") Long id,
		@Schema(example = "rafael@email.com") String email) {
}
