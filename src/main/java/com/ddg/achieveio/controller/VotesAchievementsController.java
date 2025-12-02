package com.ddg.achieveio.controller;

import com.ddg.achieveio.config.JWTUserData;
import com.ddg.achieveio.dto.response.AchievementVoteResponseDTO;
import com.ddg.achieveio.dto.response.UserVoteResponseDTO;
import com.ddg.achieveio.dto.request.VoteRequestDTO;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.service.VotesAchievementsService;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/votes/achievements")
public class VotesAchievementsController {

    private final VotesAchievementsService votesService;

    public VotesAchievementsController(VotesAchievementsService votesService) {
        this.votesService = votesService;
    }

    @PostMapping
    public ResponseEntity<UserVoteResponseDTO> vote(
            @Valid @RequestBody VoteRequestDTO requestDTO,
            @AuthenticationPrincipal JWTUserData currentUserData
    ) {

        UserVoteResponseDTO responseDTO = votesService.vote(requestDTO.achievementId(), currentUserData);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    @DeleteMapping("/{achievementId}")
    public ResponseEntity<Void> unvote(
            @PathVariable UUID achievementId,
            @AuthenticationPrincipal JWTUserData currentUserData
    ) {
        votesService.unvote(achievementId, currentUserData);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<UserVoteResponseDTO>> getVotesByUser(@PathVariable UUID userId) {
        List<UserVoteResponseDTO> responseList = votesService.getVotesByUser(userId);

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/me")
    public ResponseEntity<List<UserVoteResponseDTO>> getMyVotes(@AuthenticationPrincipal JWTUserData currentUserData) {
        List<UserVoteResponseDTO> responseList = votesService.getVotesByUser(currentUserData.userId());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/by-achievement/{achievementId}")
    public ResponseEntity<List<AchievementVoteResponseDTO>> getVotesForAchievement(@PathVariable UUID achievementId) {
        List<AchievementVoteResponseDTO> responseList = votesService.getVotesForAchievement(achievementId);

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/count/by-achievement/{achievementId}")
    public ResponseEntity<Long> getVoteCountForAchievement(@PathVariable UUID achievementId) {
        Long count = votesService.getVoteCountForAchievement(achievementId);

        return ResponseEntity.ok(count);
    }
}