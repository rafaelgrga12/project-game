package com.example.game.controller;

import com.example.game.dto.LoginResponse;
import com.example.game.dto.UsuarioRequest;
import com.example.game.model.Usuario;
import com.example.game.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Login (sem registro público)")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	@Operation(summary = "Login", description = "Valida e-mail e senha; projeto pessoal sem cadastro via API.")
	public ResponseEntity<LoginResponse> login(@RequestBody UsuarioRequest body) {
		Optional<Usuario> u = authService.autenticar(body.email(), body.senha());
		return u.map(usuario -> ResponseEntity.ok(new LoginResponse(usuario.getId(), usuario.getEmail())))
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
}
