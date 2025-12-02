package com.ddg.achieveio.controller;

import com.ddg.achieveio.dto.request.ChallengeRequestDTO;
import com.ddg.achieveio.dto.response.ChallengeResponseDTO;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.service.ChallengeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @PostMapping
    public ResponseEntity<ChallengeResponseDTO> createChallenge(
            @Valid @RequestBody ChallengeRequestDTO requestDTO,
            @AuthenticationPrincipal User currentUser
    ) {
        ChallengeResponseDTO responseDTO = challengeService.createChallenge(requestDTO, currentUser);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ChallengeResponseDTO>> getAllChallenges() {
        List<ChallengeResponseDTO> responseList = challengeService.getAllChallenges();

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeResponseDTO> getChallengeById(@PathVariable UUID id) {
        ChallengeResponseDTO responseDTO = challengeService.getChallengeById(id);

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChallengeResponseDTO> updateChallenge(
            @PathVariable UUID id,
            @Valid @RequestBody ChallengeRequestDTO requestDTO
    ) {
        ChallengeResponseDTO responseDTO = challengeService.updateChallenge(id, requestDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChallenge(
            @PathVariable UUID id
    ) {
        challengeService.deleteChallenge(id);

        return ResponseEntity.noContent().build();
    }
}