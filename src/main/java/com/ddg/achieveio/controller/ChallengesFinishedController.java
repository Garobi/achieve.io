package com.ddg.achieveio.controller;

import com.ddg.achieveio.config.JWTUserData;
import com.ddg.achieveio.dto.request.ChallengeFinishRequestDTO;
import com.ddg.achieveio.dto.response.ChallengesFinishedResponseDTO;
import com.ddg.achieveio.dto.response.UserFinishedChallengeResponseDTO;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.service.ChallengesFinishedService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/finished-challenges")
public class ChallengesFinishedController {

    private final ChallengesFinishedService finishedService;

    public ChallengesFinishedController(ChallengesFinishedService finishedService) {
        this.finishedService = finishedService;
    }

    @PostMapping
    public ResponseEntity<ChallengesFinishedResponseDTO> finishChallenge(
            @Valid @RequestBody ChallengeFinishRequestDTO requestDTO,
            @AuthenticationPrincipal JWTUserData currentUserData
    ) {
        ChallengesFinishedResponseDTO responseDTO = finishedService.finishChallenge(
                requestDTO.challengeId(),
                currentUserData
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<List<ChallengesFinishedResponseDTO>> getMyFinishedChallenges(
            @AuthenticationPrincipal JWTUserData currentUserData
    ) {
        List<ChallengesFinishedResponseDTO> responseList = finishedService.getMyFinishedChallenges(currentUserData);

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<ChallengesFinishedResponseDTO>> getFinishedChallengesByUser(
            @PathVariable UUID userId
    ) {
        List<ChallengesFinishedResponseDTO> responseList = finishedService.getFinishedChallengesByUser(userId);

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/by-challenge/{challengeId}")
    public ResponseEntity<List<UserFinishedChallengeResponseDTO>> getUsersWhoFinishedChallenge(
            @PathVariable UUID challengeId
    ) {
        List<UserFinishedChallengeResponseDTO> responseList = finishedService.getUsersWhoFinishedChallenge(challengeId);

        return ResponseEntity.ok(responseList);
    }

    @DeleteMapping("/{challengeId}")
    public ResponseEntity<Void> unfinishChallenge(
            @PathVariable UUID challengeId,
            @AuthenticationPrincipal JWTUserData currentUserData
    ) {
        finishedService.unfinishChallenge(challengeId, currentUserData);

        return ResponseEntity.noContent().build();
    }
}