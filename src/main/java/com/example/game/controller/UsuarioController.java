package com.example.game.controller;

import com.example.game.dto.UsuarioRequest;
import com.example.game.dto.UsuarioResponse;
import com.example.game.model.Usuario;
import com.example.game.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "CRUD de usuário (JSON)")
public class UsuarioController {

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@GetMapping
	@Operation(summary = "Listar todos os usuários")
	public List<UsuarioResponse> listar() {
		return usuarioService.listarTodos().stream().map(this::toResponse).toList();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar usuário por id")
	public ResponseEntity<UsuarioResponse> buscar(@PathVariable Long id) {
		return usuarioService.buscarPorId(id)
				.map(u -> ResponseEntity.ok(toResponse(u)))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	@Operation(summary = "Criar usuário")
	public ResponseEntity<UsuarioResponse> criar(@RequestBody UsuarioRequest body) {
		Usuario u = new Usuario();
		u.setEmail(body.email());
		u.setSenha(body.senha());
		Usuario salvo = usuarioService.salvar(u);
		return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(salvo));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar usuário")
	public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id, @RequestBody UsuarioRequest body) {
		return usuarioService.buscarPorId(id)
				.map(existente -> {
					existente.setEmail(body.email());
					existente.setSenha(body.senha());
					return ResponseEntity.ok(toResponse(usuarioService.salvar(existente)));
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir usuário")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		if (usuarioService.buscarPorId(id).isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		usuarioService.excluir(id);
		return ResponseEntity.noContent().build();
	}

	private UsuarioResponse toResponse(Usuario u) {
		return new UsuarioResponse(u.getId(), u.getEmail());
	}
}
