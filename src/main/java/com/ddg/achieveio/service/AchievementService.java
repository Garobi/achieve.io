package com.ddg.achieveio.service;

import com.ddg.achieveio.dto.request.AchievementRequestDTO;
import com.ddg.achieveio.dto.request.RegisterAchievementRequest;
import com.ddg.achieveio.dto.response.AchievementResponseDTO;
import com.ddg.achieveio.dto.response.RegisterAchievementResponse;
import com.ddg.achieveio.entity.Achievement;
import com.ddg.achieveio.entity.Game;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.repository.AchievementRepository;
import com.ddg.achieveio.repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final GameRepository gameRepository;

    public AchievementService(AchievementRepository achievementRepository, GameRepository gameRepository) {
        this.achievementRepository = achievementRepository;
        this.gameRepository = gameRepository;
    }

    @Transactional
    public RegisterAchievementResponse registerAchievement(
            RegisterAchievementRequest registerAchievementRequest,
            User currentUser
    ) {
        Game game = gameRepository.findById(registerAchievementRequest.game_id())
                .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado"));

        Achievement newAchievement = new Achievement();
        newAchievement.setAchievementName(registerAchievementRequest.achievement_name());
        newAchievement.setAchievementDescription(registerAchievementRequest.achievement_description());
        newAchievement.setGame(game);
        newAchievement.setVerified(false);
        newAchievement.setAuthor(currentUser);

        achievementRepository.save(newAchievement);

        return new RegisterAchievementResponse(newAchievement.getAchievementName(), newAchievement.getAchievementDescription());
    }

    @Transactional(readOnly = true)
    public List<AchievementResponseDTO> getAllAchievements() {

        List<Achievement> achievements = achievementRepository.findAllWithDetails();

        return achievements.stream()
                .map(AchievementResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AchievementResponseDTO getAchievementById(UUID id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conquista não encontrada com id: " + id));

        return new AchievementResponseDTO(achievement);
    }

    @Transactional
    public AchievementResponseDTO updateAchievement(
            UUID id,
            AchievementRequestDTO requestDTO
    ) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conquista não encontrada com id: " + id));

        Game game = gameRepository.findById(requestDTO.game_id())
                .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado"));

        achievement.setAchievementName(requestDTO.achievement_name());
        achievement.setAchievementDescription(requestDTO.achievement_description());
        achievement.setGame(game);
        achievement.setVerified(false);

        Achievement updatedAchievement = achievementRepository.save(achievement);

        return new AchievementResponseDTO(updatedAchievement);
    }


    @Transactional
    public void deleteAchievement(UUID id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conquista não encontrada com id: " + id));

        achievementRepository.delete(achievement);
    }
}