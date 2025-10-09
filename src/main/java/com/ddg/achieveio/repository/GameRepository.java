package com.ddg.achieveio.repository;

import com.ddg.achieveio.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
