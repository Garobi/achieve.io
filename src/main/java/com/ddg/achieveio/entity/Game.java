package com.ddg.achieveio.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "game_name")
    private String gameName;

    @Column(name = "game_description")
    private String gameDescription;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "game")
    private Set<Achievement> achievements;

    @ManyToMany
    @JoinTable(
            name = "games_platforms",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "platform_id")
    )
    private Set<Platform> platforms;
}
