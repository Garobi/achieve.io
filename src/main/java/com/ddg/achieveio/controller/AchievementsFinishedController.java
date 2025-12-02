package com.ddg.achieveio.controller;

import com.ddg.achieveio.config.JWTUserData;
import com.ddg.achieveio.dto.response.AchievementsFinishedResponseDTO;
import com.ddg.achieveio.dto.request.FinishRequestDTO;
import com.ddg.achieveio.dto.response.UserFinishedResponseDTO;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.service.AchievementFinishedService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/finished-achievements")
public class AchievementsFinishedController {

    private final AchievementFinishedService finishedService;

    public AchievementsFinishedController(AchievementFinishedService finishedService) {
        this.finishedService = finishedService;
    }

    @PostMapping
    public ResponseEntity<AchievementsFinishedResponseDTO> finishAchievement(
            @Valid @RequestBody FinishRequestDTO requestDTO,
            @AuthenticationPrincipal JWTUserData currentUserData
    ) {
        AchievementsFinishedResponseDTO responseDTO = finishedService.finishAchievement(
                requestDTO.achievementId(),
                currentUserData
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<List<AchievementsFinishedResponseDTO>> getMyFinishedAchievements(
            @AuthenticationPrincipal JWTUserData currentUserData
    ) {
        List<AchievementsFinishedResponseDTO> responseList = finishedService.getMyFinishedAchievements(currentUserData);

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<AchievementsFinishedResponseDTO>> getFinishedAchievementsByUser(
            @PathVariable UUID userId
    ) {
        List<AchievementsFinishedResponseDTO> responseList = finishedService.getFinishedAchievementsByUser(userId);

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/by-achievement/{achievementId}")
    public ResponseEntity<List<UserFinishedResponseDTO>> getUsersWhoFinishedAchievement(
            @PathVariable UUID achievementId
    ) {
        List<UserFinishedResponseDTO> responseList = finishedService.getUsersWhoFinishedAchievement(achievementId);

        return ResponseEntity.ok(responseList);
    }

    @DeleteMapping("/{achievementId}")
    public ResponseEntity<Void> unfinishAchievement(
            @PathVariable UUID achievementId,
            @AuthenticationPrincipal JWTUserData currentUserData
    ) {
        finishedService.unfinishAchievement(achievementId, currentUserData);

        return ResponseEntity.noContent().build();
    }
}