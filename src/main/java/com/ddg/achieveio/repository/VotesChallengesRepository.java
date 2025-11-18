package com.ddg.achieveio.repository;

import com.ddg.achieveio.entity.VotesChallenges;
import com.ddg.achieveio.entity.VotesChallengesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VotesChallengesRepository extends JpaRepository<VotesChallenges, VotesChallengesId> {

    // Busca todos os votos de um usuário
    List<VotesChallenges> findByUserId(UUID userId);

    // Busca todos os votos de um desafio
    List<VotesChallenges> findByChallengeId(UUID challengeId);

    // Contar votos de um desafio
    long countByChallengeId(UUID challengeId);
}