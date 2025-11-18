package com.ddg.achieveio.controller;

import com.ddg.achieveio.dto.request.ChallengeRequestDTO;
import com.ddg.achieveio.dto.response.ChallengeResponseDTO;
import com.ddg.achieveio.entity.Achievement;
import com.ddg.achieveio.entity.Challenge;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.repository.AchievementRepository;
import com.ddg.achieveio.repository.ChallengeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/challenges")
public class ChallengeController {

    private final ChallengeRepository challengeRepository;
    private final AchievementRepository achievementRepository;

    public ChallengeController(ChallengeRepository challengeRepository, AchievementRepository achievementRepository) {
        this.challengeRepository = challengeRepository;
        this.achievementRepository = achievementRepository;
    }

    // --- CREATE ---
    @PostMapping
    public ResponseEntity<ChallengeResponseDTO> createChallenge(
            @Valid @RequestBody ChallengeRequestDTO requestDTO,
            @AuthenticationPrincipal User currentUser
    ) {
        Set<Achievement> achievements = new HashSet<>(
                achievementRepository.findAllById(requestDTO.achievementIds())
        );

        if (achievements.size() != requestDTO.achievementIds().size()) {
            throw new EntityNotFoundException("Uma ou mais conquistas não foram encontradas.");
        }

        Challenge newChallenge = new Challenge();
        newChallenge.setChallengeName(requestDTO.challengeName());
        newChallenge.setChallengeDescription(requestDTO.challengeDescription());
        newChallenge.setAchievements(achievements);
        newChallenge.setChallengeAuthor(currentUser);
        newChallenge.setVerified(false);

        Challenge savedChallenge = challengeRepository.save(newChallenge);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ChallengeResponseDTO(savedChallenge));
    }

    @GetMapping
    public ResponseEntity<List<ChallengeResponseDTO>> getAllChallenges() {
        List<Challenge> challenges = challengeRepository.findAll();

        List<ChallengeResponseDTO> responseList = challenges.stream()
                .map(ChallengeResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeResponseDTO> getChallengeById(@PathVariable UUID id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Desafio não encontrado com id: " + id));

        return ResponseEntity.ok(new ChallengeResponseDTO(challenge));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChallengeResponseDTO> updateChallenge(
            @PathVariable UUID id,
            @Valid @RequestBody ChallengeRequestDTO requestDTO
    ) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Desafio não encontrado com id: " + id));

        Set<Achievement> achievements = new HashSet<>(
                achievementRepository.findAllById(requestDTO.achievementIds())
        );

        if (achievements.size() != requestDTO.achievementIds().size()) {
            throw new EntityNotFoundException("Uma ou mais conquistas não foram encontradas.");
        }

        challenge.setChallengeName(requestDTO.challengeName());
        challenge.setChallengeDescription(requestDTO.challengeDescription());
        challenge.setAchievements(achievements);
        challenge.setVerified(false);

        Challenge updatedChallenge = challengeRepository.save(challenge);

        return ResponseEntity.ok(new ChallengeResponseDTO(updatedChallenge));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChallenge(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Desafio não encontrado com id: " + id));

        challengeRepository.delete(challenge);

        return ResponseEntity.noContent().build();
    }
}