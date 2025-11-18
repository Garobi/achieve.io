package com.ddg.achieveio.controller;

import com.ddg.achieveio.dto.request.AchievementRequestDTO;
import com.ddg.achieveio.dto.request.RegisterAchievementRequest;
import com.ddg.achieveio.dto.response.*;
import com.ddg.achieveio.entity.Achievement;
import com.ddg.achieveio.entity.Game;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.repository.AchievementRepository;
import com.ddg.achieveio.repository.GameRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/achievements")
@Tag(name = "Achievements", description = "Gerenciamento de conquistas")
public class AchievementController {

    private final AchievementRepository achievementRepository;
    private final GameRepository gameRepository;

    public AchievementController(AchievementRepository achievementRepository, GameRepository gameRepository) {
        this.achievementRepository = achievementRepository;
        this.gameRepository = gameRepository;
    }

//    public ResponseEntity<AchievementResponse> getAchievements(){
//        return ResponseEntity.status(HttpStatus.OK).body(new AchievementResponse());
//    }

    @PostMapping("/register")
    public ResponseEntity<RegisterAchievementResponse> registerAchievement(
            @Valid @RequestBody RegisterAchievementRequest registerAchievementRequest,
            @AuthenticationPrincipal User currentUser
    ){

        Game game = gameRepository.findById(registerAchievementRequest.game_id()).orElseThrow(() ->
                new EntityNotFoundException("Jogo não encontrado"));

        Achievement newAchievement = new Achievement();
        newAchievement.setAchievementName(registerAchievementRequest.achievement_name());
        newAchievement.setAchievementDescription(registerAchievementRequest.achievement_description());
        newAchievement.setGame(game);
        newAchievement.setVerified(false);
        newAchievement.setAuthor(currentUser);
        achievementRepository.save(newAchievement);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterAchievementResponse(newAchievement.getAchievementName(), newAchievement.getAchievementDescription()));
    }

    @GetMapping
    public ResponseEntity<List<AchievementResponseDTO>> getAllAchievements(){

        List<Achievement> achievements = achievementRepository.findAll();

        List<AchievementResponseDTO> responseList = achievements.stream()
                .map(AchievementResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchievementResponseDTO> getAchievementById(@PathVariable UUID id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conquista não encontrada com id: " + id));

        return ResponseEntity.ok(new AchievementResponseDTO(achievement));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AchievementResponseDTO> updateAchievement(
            @PathVariable UUID id,
            @Valid @RequestBody AchievementRequestDTO requestDTO, // <- DTO de Request
            @AuthenticationPrincipal User currentUser
    ) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conquista não encontrada com id: " + id));

        if (!achievement.getAuthor().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Game game = gameRepository.findById(requestDTO.game_id())
                .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado"));

        achievement.setAchievementName(requestDTO.achievement_name());
        achievement.setAchievementDescription(requestDTO.achievement_description());
        achievement.setGame(game);
        achievement.setVerified(false);

        Achievement updatedAchievement = achievementRepository.save(achievement);

        return ResponseEntity.ok(new AchievementResponseDTO(updatedAchievement));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchievement(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conquista não encontrada com id: " + id));

        if (!achievement.getAuthor().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        achievementRepository.delete(achievement);

        return ResponseEntity.noContent().build();
    }

}
