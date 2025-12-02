package com.ddg.achieveio.repository;

import com.ddg.achieveio.entity.VotesChallenges;
import com.ddg.achieveio.entity.VotesChallengesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VotesChallengesRepository extends JpaRepository<VotesChallenges, VotesChallengesId> {

    List<VotesChallenges> findByUserId(UUID userId);

    List<VotesChallenges> findByChallengeId(UUID challengeId);

    long countByChallengeId(UUID challengeId);
}