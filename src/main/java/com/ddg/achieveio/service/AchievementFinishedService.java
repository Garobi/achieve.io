package com.ddg.achieveio.service;

import com.ddg.achieveio.config.JWTUserData;
import com.ddg.achieveio.dto.response.AchievementsFinishedResponseDTO;
import com.ddg.achieveio.dto.response.UserFinishedResponseDTO;
import com.ddg.achieveio.entity.Achievement;
import com.ddg.achieveio.entity.AchievementsFinished;
import com.ddg.achieveio.entity.AchievementsFinishedId;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.repository.AchievementRepository;
import com.ddg.achieveio.repository.AchievementsFinishedRepository;
import com.ddg.achieveio.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AchievementFinishedService {

    private final AchievementsFinishedRepository finishedRepository;
    private final AchievementRepository achievementRepository;
    private final UserRepository userRepository;

    public AchievementFinishedService(AchievementsFinishedRepository finishedRepository, AchievementRepository achievementRepository, UserRepository userRepository) {
        this.finishedRepository = finishedRepository;
        this.achievementRepository = achievementRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AchievementsFinishedResponseDTO finishAchievement(UUID achievementId, JWTUserData currentUserData) {
        User currentUser = userRepository.findById(currentUserData.userId());
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Conquista não encontrada"));

        AchievementsFinishedId id = new AchievementsFinishedId();
        id.setUserId(currentUserData.userId());
        id.setAchievementId(achievementId);

        if (finishedRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta conquista já foi completada");
        }

        AchievementsFinished newCompletion = new AchievementsFinished();
        newCompletion.setId(id);
        newCompletion.setUser(currentUser);
        newCompletion.setAchievement(achievement);
        newCompletion.setCompletedAt(LocalDateTime.now());

        AchievementsFinished savedCompletion = finishedRepository.save(newCompletion);

        return new AchievementsFinishedResponseDTO(savedCompletion);
    }

    @Transactional
    public List<AchievementsFinishedResponseDTO> getMyFinishedAchievements(JWTUserData currentUserData) {
        return getFinishedAchievementsByUser(currentUserData.userId());
    }

    @Transactional
    public List<AchievementsFinishedResponseDTO> getFinishedAchievementsByUser(UUID userId) {
        List<AchievementsFinished> completions = finishedRepository.findByUserId(userId);

        return completions.stream()
                .map(AchievementsFinishedResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<UserFinishedResponseDTO> getUsersWhoFinishedAchievement(UUID achievementId) {
        List<AchievementsFinished> completions = finishedRepository.findByAchievementId(achievementId);

        return completions.stream()
                .map(UserFinishedResponseDTO::new)
                .collect(Collectors.toList());
    }

    public void unfinishAchievement(UUID achievementId, JWTUserData currentUserData) {
        AchievementsFinishedId id = new AchievementsFinishedId();
        id.setUserId(currentUserData.userId());
        id.setAchievementId(achievementId);

        if (!finishedRepository.existsById(id)) {
            throw new EntityNotFoundException("Esta conquista não está marcada como finalizada");
        }

        finishedRepository.deleteById(id);
    }
}