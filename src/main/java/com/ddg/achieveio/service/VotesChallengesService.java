package com.ddg.achieveio.service;

import com.ddg.achieveio.config.JWTUserData;
import com.ddg.achieveio.dto.response.ChallengeVoteResponseDTO;
import com.ddg.achieveio.dto.response.UserVoteChallengeResponseDTO;
import com.ddg.achieveio.entity.Challenge;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.entity.VotesChallenges;
import com.ddg.achieveio.entity.VotesChallengesId;
import com.ddg.achieveio.repository.ChallengeRepository;
import com.ddg.achieveio.repository.UserRepository;
import com.ddg.achieveio.repository.VotesChallengesRepository;
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
public class VotesChallengesService {

    private final VotesChallengesRepository votesRepository;
    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;

    public VotesChallengesService(VotesChallengesRepository votesRepository, ChallengeRepository challengeRepository, UserRepository userRepository) {
        this.votesRepository = votesRepository;
        this.challengeRepository = challengeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserVoteChallengeResponseDTO vote(UUID challengeId, JWTUserData currentUserData) {
        User currentUser = userRepository.findById(currentUserData.userId());
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new EntityNotFoundException("Desafio não encontrado"));

        VotesChallengesId id = new VotesChallengesId();
        id.setUserId(currentUser.getId());
        id.setChallengeId(challengeId);

        if (votesRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Você já votou neste desafio");
        }

        VotesChallenges newVote = new VotesChallenges();
        newVote.setId(id);
        newVote.setUser(currentUser);
        newVote.setChallenge(challenge);
        newVote.setCreatedAt(LocalDateTime.now());

        VotesChallenges savedVote = votesRepository.save(newVote);

        return UserVoteChallengeResponseDTO.of(savedVote);
    }

    public void unvote(UUID challengeId, JWTUserData currentUserData) {
        User currentUser = userRepository.findById(currentUserData.userId());
        VotesChallengesId id = new VotesChallengesId();
        id.setUserId(currentUser.getId());
        id.setChallengeId(challengeId);

        if (!votesRepository.existsById(id)) {
            throw new EntityNotFoundException("Voto não encontrado");
        }

        votesRepository.deleteById(id);
    }

    @Transactional
    public List<UserVoteChallengeResponseDTO> getVotesByUser(UUID userId) {
        List<VotesChallenges> votes = votesRepository.findByUserId(userId);

        return votes.stream()
                .map(UserVoteChallengeResponseDTO::of)
                .collect(Collectors.toList());
    }

    public List<ChallengeVoteResponseDTO> getVotesForChallenge(UUID challengeId) {
        List<VotesChallenges> votes = votesRepository.findByChallengeId(challengeId);

        return votes.stream()
                .map(ChallengeVoteResponseDTO::of)
                .collect(Collectors.toList());
    }

    public Long getVoteCountForChallenge(UUID challengeId) {
        return votesRepository.countByChallengeId(challengeId);
    }
}