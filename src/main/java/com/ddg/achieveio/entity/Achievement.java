package com.ddg.achieveio.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "achievements")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "achievement_name")
    private String achievementName;

    @Column(name = "achievement_description")
    private String achievementDescription;

    private Boolean verified;

    @ManyToOne
    @JoinColumn(name = "achievement_author")
    private User author;

    @ManyToOne
    @JoinColumn(name = "game")
    private Game game;

    @ManyToMany(mappedBy = "achievements")
    private Set<Challenge> challenges;
}
