package com.example.game.service;

import com.example.game.model.Usuario;
import com.example.game.repository.UsuarioRepository;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Optional<Usuario> autenticar(String email, String senhaEmTextoPlano) {
		if (email == null || email.isBlank() || senhaEmTextoPlano == null) {
			return Optional.empty();
		}
		return usuarioRepository
				.findByEmailIgnoreCase(email.trim())
				.filter(u -> passwordEncoder.matches(senhaEmTextoPlano, u.getSenha()));
	}
}
