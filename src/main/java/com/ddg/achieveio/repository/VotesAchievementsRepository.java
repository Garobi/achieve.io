package com.ddg.achieveio.repository;

import com.ddg.achieveio.entity.VotesAchievements;
import com.ddg.achieveio.entity.VotesAchievementsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VotesAchievementsRepository extends JpaRepository<VotesAchievements, VotesAchievementsId> {

    // Busca todos os votos de um usuário
    List<VotesAchievements> findByUserId(UUID userId);

    // Busca todos os votos de uma conquista
    List<VotesAchievements> findByAchievementId(UUID achievementId);

    // (Opcional, mas útil) Contar votos de uma conquista
    long countByAchievementId(UUID achievementId);
}