package com.ddg.achieveio.repository;

import com.ddg.achieveio.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
}
