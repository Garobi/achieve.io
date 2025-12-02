package com.ddg.achieveio.service;

import com.ddg.achieveio.config.JWTUserData;
import com.ddg.achieveio.dto.response.AchievementVoteResponseDTO;
import com.ddg.achieveio.dto.response.UserVoteResponseDTO;
import com.ddg.achieveio.entity.Achievement;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.entity.VotesAchievements;
import com.ddg.achieveio.entity.VotesAchievementsId;
import com.ddg.achieveio.repository.AchievementRepository;
import com.ddg.achieveio.repository.UserRepository;
import com.ddg.achieveio.repository.VotesAchievementsRepository;
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
public class VotesAchievementsService {

    private final VotesAchievementsRepository votesRepository;
    private final AchievementRepository achievementRepository;
    private final UserRepository userRepository;

    public VotesAchievementsService(VotesAchievementsRepository votesRepository, AchievementRepository achievementRepository, UserRepository userRepository) {
        this.votesRepository = votesRepository;
        this.achievementRepository = achievementRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserVoteResponseDTO vote(UUID achievementId, JWTUserData currentUserData) {
        User currentUser = userRepository.findById(currentUserData.userId());
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Conquista não encontrada"));

        VotesAchievementsId id = new VotesAchievementsId();
        id.setUserId(currentUser.getId());
        id.setAchievementId(achievementId);

        if (votesRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Você já votou nesta conquista");
        }

        VotesAchievements newVote = new VotesAchievements();
        newVote.setId(id);
        newVote.setUser(currentUser);
        newVote.setAchievement(achievement);
        newVote.setCreatedAt(LocalDateTime.now());

        VotesAchievements savedVote = votesRepository.save(newVote);

        return UserVoteResponseDTO.of(savedVote);
    }

    public void unvote(UUID achievementId, JWTUserData currentUserData) {
        User currentUser = userRepository.findById(currentUserData.userId());
        VotesAchievementsId id = new VotesAchievementsId();
        id.setUserId(currentUser.getId());
        id.setAchievementId(achievementId);

        if (!votesRepository.existsById(id)) {
            throw new EntityNotFoundException("Voto não encontrado");
        }

        votesRepository.deleteById(id);
    }

    @Transactional
    public List<UserVoteResponseDTO> getVotesByUser(UUID userId) {
        List<VotesAchievements> votes = votesRepository.findByUserId(userId);

        return votes.stream()
                .map(UserVoteResponseDTO::of)
                .collect(Collectors.toList());
    }

    public List<AchievementVoteResponseDTO> getVotesForAchievement(UUID achievementId) {
        List<VotesAchievements> votes = votesRepository.findByAchievementId(achievementId);

        return votes.stream()
                .map(AchievementVoteResponseDTO::of)
                .collect(Collectors.toList());
    }

    public Long getVoteCountForAchievement(UUID achievementId) {
        return votesRepository.countByAchievementId(achievementId);
    }
}