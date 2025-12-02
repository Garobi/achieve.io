package com.ddg.achieveio.controller;

import com.ddg.achieveio.config.JWTUserData;
import com.ddg.achieveio.dto.response.ChallengeVoteResponseDTO;
import com.ddg.achieveio.dto.response.UserVoteChallengeResponseDTO;
import com.ddg.achieveio.dto.request.VoteChallengeRequestDTO;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.service.VotesChallengesService; // Importar o Service
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/votes/challenges")
public class VotesChallengesController {

    private final VotesChallengesService votesService;

    public VotesChallengesController(VotesChallengesService votesService) {
        this.votesService = votesService;
    }

    @PostMapping
    public ResponseEntity<UserVoteChallengeResponseDTO> vote(
            @Valid @RequestBody VoteChallengeRequestDTO requestDTO,
            @AuthenticationPrincipal JWTUserData currentUserData
    ) {
        UserVoteChallengeResponseDTO responseDTO = votesService.vote(requestDTO.challengeId(), currentUserData);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    @DeleteMapping("/{challengeId}")
    public ResponseEntity<Void> unvote(
            @PathVariable UUID challengeId,
            @AuthenticationPrincipal JWTUserData currentUserData
    ) {
        votesService.unvote(challengeId, currentUserData);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<UserVoteChallengeResponseDTO>> getVotesByUser(@PathVariable UUID userId) {
        List<UserVoteChallengeResponseDTO> responseList = votesService.getVotesByUser(userId);

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/me")
    public ResponseEntity<List<UserVoteChallengeResponseDTO>> getMyVotes(@AuthenticationPrincipal JWTUserData currentUserData) {
        List<UserVoteChallengeResponseDTO> responseList = votesService.getVotesByUser(currentUserData.userId());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/by-challenge/{challengeId}")
    public ResponseEntity<List<ChallengeVoteResponseDTO>> getVotesForChallenge(@PathVariable UUID challengeId) {
        List<ChallengeVoteResponseDTO> responseList = votesService.getVotesForChallenge(challengeId);

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/count/by-challenge/{challengeId}")
    public ResponseEntity<Long> getVoteCountForChallenge(@PathVariable UUID challengeId) {
        Long count = votesService.getVoteCountForChallenge(challengeId);

        return ResponseEntity.ok(count);
    }
}