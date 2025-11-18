package com.ddg.achieveio.controller;

import com.ddg.achieveio.dto.request.ChallengeFinishRequestDTO;
import com.ddg.achieveio.dto.response.ChallengesFinishedResponseDTO;
import com.ddg.achieveio.dto.response.UserFinishedChallengeResponseDTO;
import com.ddg.achieveio.entity.Challenge;
import com.ddg.achieveio.entity.ChallengeFinishedId;
import com.ddg.achieveio.entity.ChallengesFinished;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.repository.ChallengeRepository;
import com.ddg.achieveio.repository.ChallengesFinishedRepository;
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
@RequestMapping("/finished-challenges")
public class ChallengesFinishedController {

    private final ChallengesFinishedRepository finishedRepository;
    private final ChallengeRepository challengeRepository;

    public ChallengesFinishedController(ChallengesFinishedRepository finishedRepository, ChallengeRepository challengeRepository) {
        this.finishedRepository = finishedRepository;
        this.challengeRepository = challengeRepository;
    }

    @PostMapping
    public ResponseEntity<ChallengesFinishedResponseDTO> finishChallenge(
            @Valid @RequestBody ChallengeFinishRequestDTO requestDTO,
            @AuthenticationPrincipal User currentUser
    ) {
        Challenge challenge = challengeRepository.findById(requestDTO.challengeId())
                .orElseThrow(() -> new EntityNotFoundException("Desafio não encontrado"));

        ChallengeFinishedId id = new ChallengeFinishedId();
        id.setUserId(currentUser.getId());
        id.setChallengeId(requestDTO.challengeId());

        if (finishedRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este desafio já foi completado");
        }

        ChallengesFinished newCompletion = new ChallengesFinished();
        newCompletion.setId(id);
        newCompletion.setUser(currentUser);
        newCompletion.setChallenge(challenge);
        newCompletion.setCompletedAt(LocalDateTime.now());

        ChallengesFinished savedCompletion = finishedRepository.save(newCompletion);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ChallengesFinishedResponseDTO.of(savedCompletion));
    }

    @GetMapping("/me")
    public ResponseEntity<List<ChallengesFinishedResponseDTO>> getMyFinishedChallenges(
            @AuthenticationPrincipal User currentUser
    ) {
        List<ChallengesFinished> completions = finishedRepository.findByUserId(currentUser.getId());

        List<ChallengesFinishedResponseDTO> responseList = completions.stream()
                .map(ChallengesFinishedResponseDTO::of) // Usando o método estático 'of'
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<ChallengesFinishedResponseDTO>> getFinishedChallengesByUser(
            @PathVariable UUID userId
    ) {
        List<ChallengesFinished> completions = finishedRepository.findByUserId(userId);

        List<ChallengesFinishedResponseDTO> responseList = completions.stream()
                .map(ChallengesFinishedResponseDTO::of) // Usando o método estático 'of'
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/by-challenge/{challengeId}")
    public ResponseEntity<List<UserFinishedChallengeResponseDTO>> getUsersWhoFinishedChallenge(
            @PathVariable UUID challengeId
    ) {
        List<ChallengesFinished> completions = finishedRepository.findByChallengeId(challengeId);

        List<UserFinishedChallengeResponseDTO> responseList = completions.stream()
                .map(UserFinishedChallengeResponseDTO::of) // Usando o método estático 'of'
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @DeleteMapping("/{challengeId}")
    public ResponseEntity<Void> unfinishChallenge(
            @PathVariable UUID challengeId,
            @AuthenticationPrincipal User currentUser
    ) {
        ChallengeFinishedId id = new ChallengeFinishedId();
        id.setUserId(currentUser.getId());
        id.setChallengeId(challengeId);

        if (!finishedRepository.existsById(id)) {
            throw new EntityNotFoundException("Este desafio não está marcado como finalizado");
        }

        finishedRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}