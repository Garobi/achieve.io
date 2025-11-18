package com.ddg.achieveio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class VotesAchievementsId implements Serializable {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "achievement_id")
    private UUID achievementId;
}
