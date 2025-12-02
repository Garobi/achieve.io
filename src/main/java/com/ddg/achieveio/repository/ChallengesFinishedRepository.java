package com.ddg.achieveio.repository;

import com.ddg.achieveio.entity.ChallengeFinishedId;
import com.ddg.achieveio.entity.ChallengesFinished;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChallengesFinishedRepository extends JpaRepository<ChallengesFinished, ChallengeFinishedId> {

    List<ChallengesFinished> findByUserId(UUID userId);

    List<ChallengesFinished> findByChallengeId(UUID challengeId);
}