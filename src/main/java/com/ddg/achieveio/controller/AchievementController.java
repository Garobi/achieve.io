package com.ddg.achieveio.controller;

import com.ddg.achieveio.dto.request.RegisterAchievementRequest;
import com.ddg.achieveio.dto.response.AchievementResponse;
import com.ddg.achieveio.dto.response.RegisterAchievementResponse;
import com.ddg.achieveio.entity.Achievement;
import com.ddg.achieveio.entity.Game;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.repository.AchievementRepository;
import com.ddg.achieveio.repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/achievements")
public class AchievementController {

    private final AchievementRepository achievementRepository;
    private final GameRepository gameRepository;

    public AchievementController(AchievementRepository achievementRepository, GameRepository gameRepository) {
        this.achievementRepository = achievementRepository;
        this.gameRepository = gameRepository;
    }

    public ResponseEntity<AchievementResponse> getAchievements(){
        return ResponseEntity.status(HttpStatus.OK).body(new AchievementResponse());
    }

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



}
