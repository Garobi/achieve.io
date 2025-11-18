package com.ddg.achieveio.controller;

import com.ddg.achieveio.dto.response.AchievementsFinishedResponseDTO;
import com.ddg.achieveio.dto.request.FinishRequestDTO;
import com.ddg.achieveio.dto.response.UserFinishedResponseDTO;
import com.ddg.achieveio.entity.Achievement;
import com.ddg.achieveio.entity.AchievementsFinished;
import com.ddg.achieveio.entity.AchievementsFinishedId;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.repository.AchievementRepository;
import com.ddg.achieveio.repository.AchievementsFinishedRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/finished-achievements")
public class AchievementsFinishedController {

    private final AchievementsFinishedRepository finishedRepository;
    private final AchievementRepository achievementRepository;

    public AchievementsFinishedController(AchievementsFinishedRepository finishedRepository, AchievementRepository achievementRepository) {
        this.finishedRepository = finishedRepository;
        this.achievementRepository = achievementRepository;
    }

    @PostMapping
    public ResponseEntity<AchievementsFinishedResponseDTO> finishAchievement(
            @Valid @RequestBody FinishRequestDTO requestDTO,
            @AuthenticationPrincipal User currentUser
    ) {
        Achievement achievement = achievementRepository.findById(requestDTO.achievementId())
                .orElseThrow(() -> new EntityNotFoundException("Conquista não encontrada"));

        AchievementsFinishedId id = new AchievementsFinishedId();
        id.setUserId(currentUser.getId());
        id.setAchievementId(requestDTO.achievementId());

        if (finishedRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta conquista já foi completada");
        }

        AchievementsFinished newCompletion = new AchievementsFinished();
        newCompletion.setId(id);
        newCompletion.setUser(currentUser);
        newCompletion.setAchievement(achievement);
        newCompletion.setCompletedAt(LocalDateTime.now());

        AchievementsFinished savedCompletion = finishedRepository.save(newCompletion);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AchievementsFinishedResponseDTO(savedCompletion));
    }

    @GetMapping("/me")
    public ResponseEntity<List<AchievementsFinishedResponseDTO>> getMyFinishedAchievements(
            @AuthenticationPrincipal User currentUser
    ) {
        List<AchievementsFinished> completions = finishedRepository.findByUserId(currentUser.getId());

        List<AchievementsFinishedResponseDTO> responseList = completions.stream()
                .map(AchievementsFinishedResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<AchievementsFinishedResponseDTO>> getFinishedAchievementsByUser(
            @PathVariable UUID userId
    ) {
        List<AchievementsFinished> completions = finishedRepository.findByUserId(userId);

        List<AchievementsFinishedResponseDTO> responseList = completions.stream()
                .map(AchievementsFinishedResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/by-achievement/{achievementId}")
    public ResponseEntity<List<UserFinishedResponseDTO>> getUsersWhoFinishedAchievement(
            @PathVariable UUID achievementId
    ) {
        List<AchievementsFinished> completions = finishedRepository.findByAchievementId(achievementId);

        List<UserFinishedResponseDTO> responseList = completions.stream()
                .map(UserFinishedResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @DeleteMapping("/{achievementId}")
    public ResponseEntity<Void> unfinishAchievement(
            @PathVariable UUID achievementId,
            @AuthenticationPrincipal User currentUser
    ) {
        AchievementsFinishedId id = new AchievementsFinishedId();
        id.setUserId(currentUser.getId());
        id.setAchievementId(achievementId);

        if (!finishedRepository.existsById(id)) {
            throw new EntityNotFoundException("Esta conquista não está marcada como finalizada");
        }

        finishedRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}