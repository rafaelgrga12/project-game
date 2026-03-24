package com.example.game.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "E-mail e senha (criação ou atualização)")
public record UsuarioRequest(
		@Schema(example = "rafael@email.com") String email,
		@Schema(example = "gamess12345") String senha) {
}
