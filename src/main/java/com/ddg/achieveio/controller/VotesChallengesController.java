package com.ddg.achieveio.controller;

import com.ddg.achieveio.dto.response.ChallengeVoteResponseDTO;
import com.ddg.achieveio.dto.response.UserVoteChallengeResponseDTO;
import com.ddg.achieveio.dto.request.VoteChallengeRequestDTO;
import com.ddg.achieveio.entity.Challenge;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.entity.VotesChallenges;
import com.ddg.achieveio.entity.VotesChallengesId;
import com.ddg.achieveio.repository.ChallengeRepository;
import com.ddg.achieveio.repository.VotesChallengesRepository;
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
@RequestMapping("/votes/challenges")
public class VotesChallengesController {

    private final VotesChallengesRepository votesRepository;
    private final ChallengeRepository challengeRepository;

    public VotesChallengesController(VotesChallengesRepository votesRepository, ChallengeRepository challengeRepository) {
        this.votesRepository = votesRepository;
        this.challengeRepository = challengeRepository;
    }

    @PostMapping
    public ResponseEntity<UserVoteChallengeResponseDTO> vote(
            @Valid @RequestBody VoteChallengeRequestDTO requestDTO,
            @AuthenticationPrincipal User currentUser
    ) {
        Challenge challenge = challengeRepository.findById(requestDTO.challengeId())
                .orElseThrow(() -> new EntityNotFoundException("Desafio não encontrado"));

        VotesChallengesId id = new VotesChallengesId();
        id.setUserId(currentUser.getId());
        id.setChallengeId(challenge.getId());

        if (votesRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Você já votou neste desafio");
        }

        VotesChallenges newVote = new VotesChallenges();
        newVote.setId(id);
        newVote.setUser(currentUser);
        newVote.setChallenge(challenge);
        newVote.setCreatedAt(LocalDateTime.now());

        VotesChallenges savedVote = votesRepository.save(newVote);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserVoteChallengeResponseDTO.of(savedVote));
    }

    @DeleteMapping("/{challengeId}")
    public ResponseEntity<Void> unvote(
            @PathVariable UUID challengeId,
            @AuthenticationPrincipal User currentUser
    ) {
        VotesChallengesId id = new VotesChallengesId();
        id.setUserId(currentUser.getId());
        id.setChallengeId(challengeId);

        if (!votesRepository.existsById(id)) {
            throw new EntityNotFoundException("Voto não encontrado");
        }

        votesRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<UserVoteChallengeResponseDTO>> getVotesByUser(@PathVariable UUID userId) {
        List<VotesChallenges> votes = votesRepository.findByUserId(userId);

        List<UserVoteChallengeResponseDTO> responseList = votes.stream()
                .map(UserVoteChallengeResponseDTO::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/me")
    public ResponseEntity<List<UserVoteChallengeResponseDTO>> getMyVotes(@AuthenticationPrincipal User currentUser) {
        return getVotesByUser(currentUser.getId());
    }

    @GetMapping("/by-challenge/{challengeId}")
    public ResponseEntity<List<ChallengeVoteResponseDTO>> getVotesForChallenge(@PathVariable UUID challengeId) {
        List<VotesChallenges> votes = votesRepository.findByChallengeId(challengeId);

        List<ChallengeVoteResponseDTO> responseList = votes.stream()
                .map(ChallengeVoteResponseDTO::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/count/by-challenge/{challengeId}")
    public ResponseEntity<Long> getVoteCountForChallenge(@PathVariable UUID challengeId) {
        long count = votesRepository.countByChallengeId(challengeId);
        return ResponseEntity.ok(count);
    }
}