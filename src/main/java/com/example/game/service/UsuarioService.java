package com.example.game.service;

import com.example.game.model.Usuario;
import com.example.game.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public List<Usuario> listarTodos() {
		return usuarioRepository.findAll();
	}

	public Optional<Usuario> buscarPorId(Long id) {
		return usuarioRepository.findById(id);
	}

	@Transactional
	public Usuario salvar(Usuario usuario) {
		if (usuario.getSenha() != null && !usuario.getSenha().startsWith("$2")) {
			usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		}
		return usuarioRepository.save(usuario);
	}

	@Transactional
	public void excluir(Long id) {
		usuarioRepository.deleteById(id);
	}
}
