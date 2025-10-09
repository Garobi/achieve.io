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
class ChallengeFinishedId implements Serializable {

    @Column(name = "challenge_id")
    private UUID challengeId;

    @Column(name = "user_id")
    private UUID  userId;
}

@Entity
@Getter
@Setter
@Table(name = "challenges_finished")
public class ChallengesFinished {
    @EmbeddedId
    private ChallengeFinishedId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("challengeId")
    @JoinColumn(name = "challenge_id", insertable = false, updatable = false)
    private Challenge challenge;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
