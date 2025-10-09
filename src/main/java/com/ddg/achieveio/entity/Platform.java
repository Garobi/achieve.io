package com.ddg.achieveio.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "platforms")
public class Platform {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "platform_name")
    private String platformName;

    @Column(name = "platform_description")
    private String platformDescription;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToMany(mappedBy = "platforms")
    private Set<Game> games;

}
