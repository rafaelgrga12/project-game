package com.example.game.dto;

import com.example.game.model.StatusJogo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Dados do jogo (modal e atualização)")
public record JogoRequest(
		@Schema(example = "Meu jogo") String titulo,
		@Schema(example = "RPG de Ação") String genero,
		@Schema(example = "9.5") BigDecimal nota,
		@Schema(example = "https://store.example.com/jogo") String link,
		@Schema(example = "https://example.com/capa.jpg") String imagem,
		@Schema(description = "Opcional na criação (padrão A_JOGAR)") StatusJogo status) {
}
