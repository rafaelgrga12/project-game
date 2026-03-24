package com.example.game.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "jogos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Jogo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 500)
	private String titulo;

	@Column(length = 200)
	private String genero;

	@Column(precision = 3, scale = 1)
	private BigDecimal nota;

	@Column(name = "link_loja", length = 2000)
	private String linkLoja;

	@Column(name = "url_imagem", length = 2000)
	private String urlImagem;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private StatusJogo status = StatusJogo.A_JOGAR;
}
