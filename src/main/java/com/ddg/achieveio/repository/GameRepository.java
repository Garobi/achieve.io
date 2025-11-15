package com.ddg.achieveio.repository;

import com.ddg.achieveio.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;
import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {

    boolean existsByGameNameIgnoreCase(String gameName);

    Optional<Game> findByGameNameIgnoreCase(String gameName);

    @EntityGraph(attributePaths = {"platforms"})
    Page<Game> findByGameNameContainingIgnoreCase(String gameName, Pageable pageable);
}
