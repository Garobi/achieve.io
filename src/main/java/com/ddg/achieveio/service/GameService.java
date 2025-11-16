package com.ddg.achieveio.service;

import com.ddg.achieveio.dto.request.CreateGameRequest;
import com.ddg.achieveio.dto.request.UpdateGameRequest;
import com.ddg.achieveio.dto.response.GameResponse;
import com.ddg.achieveio.entity.Game;
import com.ddg.achieveio.entity.Platform;
import com.ddg.achieveio.repository.GameRepository;
import com.ddg.achieveio.repository.PlatformRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class GameService {

    private final GameRepository gameRepository;
    private final PlatformRepository platformRepository;

    public GameService(GameRepository gameRepository, PlatformRepository platformRepository) {
        this.gameRepository = gameRepository;
        this.platformRepository = platformRepository;
    }

    @Transactional
    public GameResponse createGame(CreateGameRequest request) {
        validateGameNameUniqueness(request.gameName());

        Game game = buildGameFromRequest(request);
        Game savedGame = gameRepository.save(game);
        
        return GameResponse.fromEntity(savedGame);
    }

    public GameResponse getGameById(UUID id) {
        Game game = findGameOrThrow(id);
        return GameResponse.fromEntity(game);
    }

    public Page<GameResponse> getAllGames(Pageable pageable) {
        return gameRepository.findAll(pageable)
                .map(GameResponse::fromEntity);
    }

    public Page<GameResponse> searchGames(String search, Pageable pageable) {
        if (search == null || search.isBlank()) {
            return getAllGames(pageable);
        }
        
        return gameRepository.findByGameNameContainingIgnoreCase(search, pageable)
                .map(GameResponse::fromEntity);
    }

    @Transactional
    public GameResponse updateGame(UUID id, UpdateGameRequest request) {
        Game game = findGameOrThrow(id);

        updateGameName(game, request.gameName());
        updateGameDescription(game, request.gameDescription());
        updateImageUrl(game, request.imageUrl());
        updatePlatforms(game, request.platformIds());

        Game updatedGame = gameRepository.save(game);
        return GameResponse.fromEntity(updatedGame);
    }

    @Transactional
    public void deleteGame(UUID id) {
        Game game = findGameOrThrow(id);
        validateGameHasNoAchievements(game);
        gameRepository.delete(game);
    }

    private Game buildGameFromRequest(CreateGameRequest request) {
        Game game = new Game();
        game.setGameName(request.gameName());
        game.setGameDescription(request.gameDescription());
        game.setImageUrl(request.imageUrl());

        if (request.platformIds() != null && !request.platformIds().isEmpty()) {
            game.setPlatforms(findPlatformsByIds(request.platformIds()));
        }

        return game;
    }

    private void updateGameName(Game game, String newName) {
        if (newName != null && !newName.equals(game.getGameName())) {
            validateGameNameUniqueness(newName);
            game.setGameName(newName);
        }
    }

    private void updateGameDescription(Game game, String description) {
        if (description != null) {
            game.setGameDescription(description);
        }
    }

    private void updateImageUrl(Game game, String imageUrl) {
        if (imageUrl != null) {
            game.setImageUrl(imageUrl);
        }
    }

    private void updatePlatforms(Game game, Set<UUID> platformIds) {
        if (platformIds != null) {
            Set<Platform> platforms = platformIds.isEmpty() 
                ? new HashSet<>() 
                : findPlatformsByIds(platformIds);
            game.setPlatforms(platforms);
        }
    }

    private Set<Platform> findPlatformsByIds(Set<UUID> platformIds) {
        Set<Platform> platforms = new HashSet<>(platformRepository.findAllById(platformIds));
        
        if (platforms.size() != platformIds.size()) {
            throw new EntityNotFoundException("Uma ou mais plataformas não foram encontradas");
        }
        
        return platforms;
    }

    private Game findGameOrThrow(UUID id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado com ID: " + id));
    }

    private void validateGameNameUniqueness(String gameName) {
        if (gameRepository.existsByGameNameIgnoreCase(gameName)) {
            throw new IllegalArgumentException("Já existe um jogo com este nome");
        }
    }

    private void validateGameHasNoAchievements(Game game) {
        if (game.getAchievements() != null && !game.getAchievements().isEmpty()) {
            throw new IllegalStateException("Não é possível excluir um jogo com conquistas associadas");
        }
    }
}