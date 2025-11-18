package com.ddg.achieveio.controller;

import com.ddg.achieveio.dto.response.AchievementVoteResponseDTO;
import com.ddg.achieveio.dto.response.UserVoteResponseDTO;
import com.ddg.achieveio.dto.request.VoteRequestDTO;
import com.ddg.achieveio.entity.Achievement;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.entity.VotesAchievements;
import com.ddg.achieveio.entity.VotesAchievementsId;
import com.ddg.achieveio.repository.AchievementRepository;
import com.ddg.achieveio.repository.VotesAchievementsRepository;
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
@RequestMapping("/votes/achievements")
public class VotesAchievementsController {

    private final VotesAchievementsRepository votesRepository;
    private final AchievementRepository achievementRepository;

    public VotesAchievementsController(VotesAchievementsRepository votesRepository, AchievementRepository achievementRepository) {
        this.votesRepository = votesRepository;
        this.achievementRepository = achievementRepository;
    }

    @PostMapping
    public ResponseEntity<UserVoteResponseDTO> vote(
            @Valid @RequestBody VoteRequestDTO requestDTO,
            @AuthenticationPrincipal User currentUser
    ) {
        Achievement achievement = achievementRepository.findById(requestDTO.achievementId())
                .orElseThrow(() -> new EntityNotFoundException("Conquista não encontrada"));

        VotesAchievementsId id = new VotesAchievementsId();
        id.setUserId(currentUser.getId());
        id.setAchievementId(achievement.getId());

        if (votesRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Você já votou nesta conquista");
        }

        VotesAchievements newVote = new VotesAchievements();
        newVote.setId(id);
        newVote.setUser(currentUser);
        newVote.setAchievement(achievement);
        newVote.setCreatedAt(LocalDateTime.now());

        VotesAchievements savedVote = votesRepository.save(newVote);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserVoteResponseDTO.of(savedVote));
    }

    @DeleteMapping("/{achievementId}")
    public ResponseEntity<Void> unvote(
            @PathVariable UUID achievementId,
            @AuthenticationPrincipal User currentUser
    ) {
        VotesAchievementsId id = new VotesAchievementsId();
        id.setUserId(currentUser.getId());
        id.setAchievementId(achievementId);

        if (!votesRepository.existsById(id)) {
            throw new EntityNotFoundException("Voto não encontrado");
        }

        votesRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<UserVoteResponseDTO>> getVotesByUser(@PathVariable UUID userId) {
        List<VotesAchievements> votes = votesRepository.findByUserId(userId);

        List<UserVoteResponseDTO> responseList = votes.stream()
                .map(UserVoteResponseDTO::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/me")
    public ResponseEntity<List<UserVoteResponseDTO>> getMyVotes(@AuthenticationPrincipal User currentUser) {
        return getVotesByUser(currentUser.getId());
    }

    @GetMapping("/by-achievement/{achievementId}")
    public ResponseEntity<List<AchievementVoteResponseDTO>> getVotesForAchievement(@PathVariable UUID achievementId) {
        List<VotesAchievements> votes = votesRepository.findByAchievementId(achievementId);

        List<AchievementVoteResponseDTO> responseList = votes.stream()
                .map(AchievementVoteResponseDTO::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/count/by-achievement/{achievementId}")
    public ResponseEntity<Long> getVoteCountForAchievement(@PathVariable UUID achievementId) {
        long count = votesRepository.countByAchievementId(achievementId);
        return ResponseEntity.ok(count);
    }
}