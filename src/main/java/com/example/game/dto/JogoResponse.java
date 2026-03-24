package com.example.game.dto;

import com.example.game.model.StatusJogo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "J.persistido")
public record JogoResponse(
		@Schema(example = "1") Long id,
		@Schema(example = "Elden Ring") String titulo,
		@Schema(example = "RPG de Ação") String genero,
		@Schema(example = "9.5") BigDecimal nota,
		@Schema(example = "https://...") String link,
		@Schema(example = "https://...") String imagem,
		@Schema(example = "A_JOGAR") StatusJogo status) {
}
