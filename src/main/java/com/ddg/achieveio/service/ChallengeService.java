package com.ddg.achieveio.service;

import com.ddg.achieveio.dto.request.ChallengeRequestDTO;
import com.ddg.achieveio.dto.response.ChallengeResponseDTO;
import com.ddg.achieveio.entity.Achievement;
import com.ddg.achieveio.entity.Challenge;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.repository.AchievementRepository;
import com.ddg.achieveio.repository.ChallengeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final AchievementRepository achievementRepository;

    public ChallengeService(ChallengeRepository challengeRepository, AchievementRepository achievementRepository) {
        this.challengeRepository = challengeRepository;
        this.achievementRepository = achievementRepository;
    }

    @Transactional
    public ChallengeResponseDTO createChallenge(ChallengeRequestDTO requestDTO, User currentUser) {
        Set<Achievement> achievements = loadAchievements(requestDTO.achievementIds());

        Challenge newChallenge = new Challenge();
        newChallenge.setChallengeName(requestDTO.challengeName());
        newChallenge.setChallengeDescription(requestDTO.challengeDescription());
        newChallenge.setAchievements(achievements);
        newChallenge.setChallengeAuthor(currentUser);
        newChallenge.setVerified(false);

        Challenge savedChallenge = challengeRepository.save(newChallenge);
        return new ChallengeResponseDTO(savedChallenge);
    }

    @Transactional(readOnly = true)
    public List<ChallengeResponseDTO> getAllChallenges() {
        List<Challenge> challenges = challengeRepository.findAllWithDetails();

        return challenges.stream()
                .map(ChallengeResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ChallengeResponseDTO getChallengeById(UUID id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Desafio não encontrado com id: " + id));

        return new ChallengeResponseDTO(challenge);
    }

    @Transactional
    public ChallengeResponseDTO updateChallenge(UUID id, ChallengeRequestDTO requestDTO) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Desafio não encontrado com id: " + id));

        Set<Achievement> achievements = loadAchievements(requestDTO.achievementIds());

        challenge.setChallengeName(requestDTO.challengeName());
        challenge.setChallengeDescription(requestDTO.challengeDescription());
        challenge.setAchievements(achievements);
        challenge.setVerified(false);

        Challenge updatedChallenge = challengeRepository.save(challenge);
        return new ChallengeResponseDTO(updatedChallenge);
    }

    @Transactional
    public void deleteChallenge(UUID id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Desafio não encontrado com id: " + id));

        challengeRepository.delete(challenge);
    }

    @Transactional
    private Set<Achievement> loadAchievements(Set<UUID> achievementIds) {
        Iterable<Achievement> achievementsIterable = achievementRepository.findAllById(achievementIds);
        Set<Achievement> achievements = StreamSupport.stream(achievementsIterable.spliterator(), false)
                .collect(Collectors.toSet());

        if (achievements.size() != achievementIds.size()) {
            throw new EntityNotFoundException("Uma ou mais conquistas não foram encontradas.");
        }
        return achievements;
    }
}