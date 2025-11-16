package com.ddg.achieveio.controller;

import com.ddg.achieveio.dto.request.CreateGameRequest;
import com.ddg.achieveio.dto.request.UpdateGameRequest;
import com.ddg.achieveio.dto.response.GameResponse;
import com.ddg.achieveio.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/games")
@Tag(name = "Games", description = "Gerenciamento de jogos")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar novo jogo", security = @SecurityRequirement(name = "bearer-jwt"))
    public GameResponse createGame(@Valid @RequestBody CreateGameRequest request) {
        return gameService.createGame(request);
    }

    @GetMapping
    @Operation(summary = "Listar todos os jogos com paginação")
    public Page<GameResponse> getAllGames(@PageableDefault(size = 20, sort = "gameName") Pageable pageable) {
        return gameService.getAllGames(pageable);
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar jogos por nome")
    public Page<GameResponse> searchGames(
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = "gameName") Pageable pageable) {
        return gameService.searchGames(q, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar jogo por ID")
    public GameResponse getGameById(@PathVariable UUID id) {
        return gameService.getGameById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar jogo", security = @SecurityRequirement(name = "bearer-jwt"))
    public GameResponse updateGame(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateGameRequest request) {
        return gameService.updateGame(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar jogo", security = @SecurityRequirement(name = "bearer-jwt"))
    public void deleteGame(@PathVariable UUID id) {
        gameService.deleteGame(id);
    }
}