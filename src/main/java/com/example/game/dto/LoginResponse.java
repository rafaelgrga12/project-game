package com.example.game.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Sessão após login")
public record LoginResponse(@Schema(example = "1") Long id, @Schema(example = "eu@local.dev") String email) {
}
