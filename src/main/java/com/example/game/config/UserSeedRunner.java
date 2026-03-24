package com.example.game.config;

import com.example.game.model.Usuario;
import com.example.game.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserSeedRunner implements CommandLineRunner {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;
	private final Environment environment;

	public UserSeedRunner(
			UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, Environment environment) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.environment = environment;
	}

	@Override
	public void run(String... args) {
		if (usuarioRepository.count() > 0) {
			return;
		}
		String email = environment.getProperty("app.user.seed-email", "eu@local.dev");
		String rawPassword = environment.getProperty("app.user.seed-password", "123456");
		Usuario u = new Usuario();
		u.setEmail(email.trim());
		u.setSenha(passwordEncoder.encode(rawPassword));
		usuarioRepository.save(u);
	}
}
