package com.ddg.achieveio.repository;

import com.ddg.achieveio.entity.Challenge;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ChallengeRepository  extends JpaRepository<Challenge, UUID> {
    @EntityGraph(
            attributePaths = {
                    "challengeAuthor",
                    "achievements"
            }
    )
    @Query("SELECT c FROM Challenge c")
    List<Challenge> findAllWithDetails();
}