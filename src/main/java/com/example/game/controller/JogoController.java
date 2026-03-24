package com.example.game.controller;

import com.example.game.dto.JogoRequest;
import com.example.game.dto.JogoResponse;
import com.example.game.model.Jogo;
import com.example.game.model.StatusJogo;
import com.example.game.service.JogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jogos")
@Tag(name = "Jogos", description = "CRUD de jogos (campos do modal)")
public class JogoController {

	private final JogoService jogoService;

	public JogoController(JogoService jogoService) {
		this.jogoService = jogoService;
	}

	@GetMapping
	@Operation(summary = "Listar jogos", description = "Opcionalmente filtre por status: A_JOGAR ou CONCLUIDO")
	public List<JogoResponse> listar(
			@Parameter(description = "A_JOGAR ou CONCLUIDO")
			@RequestParam(required = false) StatusJogo status) {
		if (status == null) {
			return jogoService.listarTodos().stream().map(this::toResponse).toList();
		}
		return jogoService.listarPorStatus(status).stream().map(this::toResponse).toList();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar jogo por id")
	public ResponseEntity<JogoResponse> buscar(@PathVariable Long id) {
		return jogoService.buscarPorId(id)
				.map(j -> ResponseEntity.ok(toResponse(j)))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	@Operation(summary = "Criar jogo (modal Adicionar Jogo)")
	public ResponseEntity<JogoResponse> criar(@RequestBody JogoRequest body) {
		Jogo salvo = jogoService.criar(body);
		return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(salvo));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar jogo (use status CONCLUIDO para concluir)")
	public ResponseEntity<JogoResponse> atualizar(@PathVariable Long id, @RequestBody JogoRequest body) {
		Jogo atualizado = jogoService.atualizar(id, body);
		return ResponseEntity.ok(toResponse(atualizado));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir jogo")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		jogoService.excluir(id);
		return ResponseEntity.noContent().build();
	}

	private JogoResponse toResponse(Jogo j) {
		return new JogoResponse(
				j.getId(),
				j.getTitulo(),
				j.getGenero(),
				j.getNota(),
				j.getLinkLoja(),
				j.getUrlImagem(),
				j.getStatus());
	}
}
