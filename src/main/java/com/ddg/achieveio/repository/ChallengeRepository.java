package com.ddg.achieveio.repository;

import com.ddg.achieveio.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChallengeRepository  extends JpaRepository<Challenge, UUID> {
}