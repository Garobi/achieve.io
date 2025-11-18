package com.ddg.achieveio.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "votes_challenges")
public class VotesChallenges {
    @EmbeddedId
    private VotesChallengesId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("challengeId")
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
