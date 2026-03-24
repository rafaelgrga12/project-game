package com.example.game.repository;

import com.example.game.model.Jogo;
import com.example.game.model.StatusJogo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JogoRepository extends JpaRepository<Jogo, Long> {

	List<Jogo> findByStatusOrderByIdAsc(StatusJogo status);
}
