package com.ddg.achieveio.service;

import com.ddg.achieveio.config.JWTUserData;
import com.ddg.achieveio.dto.response.ChallengesFinishedResponseDTO;
import com.ddg.achieveio.dto.response.UserFinishedChallengeResponseDTO;
import com.ddg.achieveio.entity.Challenge;
import com.ddg.achieveio.entity.ChallengeFinishedId;
import com.ddg.achieveio.entity.ChallengesFinished;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.repository.ChallengeRepository;
import com.ddg.achieveio.repository.ChallengesFinishedRepository;
import com.ddg.achieveio.repository.UserRepository;
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
public class ChallengesFinishedService {

    private final ChallengesFinishedRepository finishedRepository;
    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;

    public ChallengesFinishedService(ChallengesFinishedRepository finishedRepository, ChallengeRepository challengeRepository, UserRepository userRepository) {
        this.finishedRepository = finishedRepository;
        this.challengeRepository = challengeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ChallengesFinishedResponseDTO finishChallenge(UUID challengeId, JWTUserData currentUserData) {
        User currentUser = userRepository.findById(currentUserData.userId());
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new EntityNotFoundException("Desafio não encontrado"));

        ChallengeFinishedId id = new ChallengeFinishedId();
        id.setUserId(currentUser.getId());
        id.setChallengeId(challengeId);

        if (finishedRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este desafio já foi completado");
        }

        ChallengesFinished newCompletion = new ChallengesFinished();
        newCompletion.setId(id);
        newCompletion.setUser(currentUser);
        newCompletion.setChallenge(challenge);
        newCompletion.setCompletedAt(LocalDateTime.now());

        ChallengesFinished savedCompletion = finishedRepository.save(newCompletion);
        return ChallengesFinishedResponseDTO.of(savedCompletion);
    }

    @Transactional
    public List<ChallengesFinishedResponseDTO> getMyFinishedChallenges(JWTUserData currentUserData) {
        return getFinishedChallengesByUser(currentUserData.userId());
    }

    @Transactional
    public List<ChallengesFinishedResponseDTO> getFinishedChallengesByUser(UUID userId) {
        List<ChallengesFinished> completions = finishedRepository.findByUserId(userId);

        return completions.stream()
                .map(ChallengesFinishedResponseDTO::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<UserFinishedChallengeResponseDTO> getUsersWhoFinishedChallenge(UUID challengeId) {
        List<ChallengesFinished> completions = finishedRepository.findByChallengeId(challengeId);

        return completions.stream()
                .map(UserFinishedChallengeResponseDTO::of)
                .collect(Collectors.toList());
    }

    public void unfinishChallenge(UUID challengeId, JWTUserData currentUserData) {
        ChallengeFinishedId id = new ChallengeFinishedId();
        id.setUserId(currentUserData.userId());
        id.setChallengeId(challengeId);

        if (!finishedRepository.existsById(id)) {
            throw new EntityNotFoundException("Este desafio não está marcado como finalizado");
        }

        finishedRepository.deleteById(id);
    }
}