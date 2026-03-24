package com.example.game.service;

import com.example.game.dto.JogoRequest;
import com.example.game.model.Jogo;
import com.example.game.model.StatusJogo;
import com.example.game.repository.JogoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class JogoService {

	private final JogoRepository jogoRepository;

	public JogoService(JogoRepository jogoRepository) {
		this.jogoRepository = jogoRepository;
	}

	public List<Jogo> listarTodos() {
		return jogoRepository.findAll();
	}

	public List<Jogo> listarPorStatus(StatusJogo status) {
		return jogoRepository.findByStatusOrderByIdAsc(status);
	}

	public Optional<Jogo> buscarPorId(Long id) {
		return jogoRepository.findById(id);
	}

	@Transactional
	public Jogo criar(JogoRequest body) {
		validarTitulo(body.titulo());
		Jogo j = new Jogo();
		aplicar(j, body);
		if (body.status() != null) {
			j.setStatus(body.status());
		} else {
			j.setStatus(StatusJogo.A_JOGAR);
		}
		return jogoRepository.save(j);
	}

	@Transactional
	public Jogo atualizar(Long id, JogoRequest body) {
		Jogo j = jogoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogo não encontrado"));
		validarTitulo(body.titulo());
		aplicar(j, body);
		if (body.status() != null) {
			j.setStatus(body.status());
		}
		return jogoRepository.save(j);
	}

	@Transactional
	public void excluir(Long id) {
		if (!jogoRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogo não encontrado");
		}
		jogoRepository.deleteById(id);
	}

	private void validarTitulo(String titulo) {
		if (titulo == null || titulo.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "titulo é obrigatório");
		}
	}

	private void aplicar(Jogo j, JogoRequest body) {
		j.setTitulo(body.titulo().trim());
		j.setGenero(blankToNull(body.genero()));
		j.setNota(body.nota());
		j.setLinkLoja(blankToNull(body.link()));
		j.setUrlImagem(blankToNull(body.imagem()));
	}

	private static String blankToNull(String s) {
		if (s == null || s.isBlank()) {
			return null;
		}
		return s.trim();
	}
}
