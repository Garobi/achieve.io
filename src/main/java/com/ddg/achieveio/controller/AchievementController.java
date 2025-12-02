package com.ddg.achieveio.controller;

import com.ddg.achieveio.dto.request.AchievementRequestDTO;
import com.ddg.achieveio.dto.request.RegisterAchievementRequest;
import com.ddg.achieveio.dto.response.AchievementResponseDTO;
import com.ddg.achieveio.dto.response.RegisterAchievementResponse;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.service.AchievementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/achievements")
@Tag(name = "Achievements", description = "Gerenciamento de conquistas")
public class AchievementController {

    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }


    @PostMapping("/register")
    public ResponseEntity<RegisterAchievementResponse> registerAchievement(
            @Valid @RequestBody RegisterAchievementRequest request,
            @AuthenticationPrincipal User currentUser
    ){
        RegisterAchievementResponse response = achievementService.registerAchievement(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AchievementResponseDTO>> getAllAchievements(){
        List<AchievementResponseDTO> responseList = achievementService.getAllAchievements();
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchievementResponseDTO> getAchievementById(@PathVariable UUID id) {
        AchievementResponseDTO response = achievementService.getAchievementById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AchievementResponseDTO> updateAchievement(
            @PathVariable UUID id,
            @Valid @RequestBody AchievementRequestDTO requestDTO
    ) {
        AchievementResponseDTO response = achievementService.updateAchievement(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchievement(
            @PathVariable UUID id
    ) {
        achievementService.deleteAchievement(id);
        return ResponseEntity.noContent().build();
    }

}