package com.ddg.achieveio.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
class AchievementsFinishedId implements Serializable {

    @Column(name = "achievement_id")
    private UUID achievementId;

    @Column(name = "user_id")
    private UUID userId;
}

@Entity
@Getter
@Setter
@Table(name = "achievements_finished")
public class AchievementsFinished {
    @EmbeddedId
    private AchievementsFinishedId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("achievementId")
    @JoinColumn(name = "achievement_id", insertable = false, updatable = false)
    private Achievement achievement;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
