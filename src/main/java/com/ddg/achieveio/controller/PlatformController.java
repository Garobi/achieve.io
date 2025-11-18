package com.ddg.achieveio.controller;

import com.ddg.achieveio.dto.request.PlatformRequestDTO;
import com.ddg.achieveio.dto.response.PlatformResponseDTO;
import com.ddg.achieveio.entity.Platform;
import com.ddg.achieveio.repository.PlatformRepository; // Você precisa criar este repository
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/platforms")
public class PlatformController {

    private final PlatformRepository platformRepository;

    public PlatformController(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    @PostMapping
    public ResponseEntity<PlatformResponseDTO> createPlatform(
            @Valid @RequestBody PlatformRequestDTO requestDTO
    ) {

        Platform newPlatform = new Platform();
        newPlatform.setPlatformName(requestDTO.platformName());
        newPlatform.setPlatformDescription(requestDTO.platformDescription());
        newPlatform.setImageUrl(requestDTO.imageUrl());

        Platform savedPlatform = platformRepository.save(newPlatform);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new PlatformResponseDTO(savedPlatform));
    }

    @GetMapping
    public ResponseEntity<List<PlatformResponseDTO>> getAllPlatforms() {
        List<Platform> platforms = platformRepository.findAll();

        List<PlatformResponseDTO> responseList = platforms.stream()
                .map(PlatformResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    // --- READ (One) ---
    @GetMapping("/{id}")
    public ResponseEntity<PlatformResponseDTO> getPlatformById(@PathVariable UUID id) {
        Platform platform = platformRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plataforma não encontrada com id: " + id));

        return ResponseEntity.ok(new PlatformResponseDTO(platform));
    }

    // --- UPDATE ---
    @PutMapping("/{id}")
    public ResponseEntity<PlatformResponseDTO> updatePlatform(
            @PathVariable UUID id,
            @Valid @RequestBody PlatformRequestDTO requestDTO
    ) {

        Platform platform = platformRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plataforma não encontrada com id: " + id));

        platform.setPlatformName(requestDTO.platformName());
        platform.setPlatformDescription(requestDTO.platformDescription());
        platform.setImageUrl(requestDTO.imageUrl());

        Platform updatedPlatform = platformRepository.save(platform);

        return ResponseEntity.ok(new PlatformResponseDTO(updatedPlatform));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlatform(@PathVariable UUID id) {

        Platform platform = platformRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plataforma não encontrada com id: " + id));

        platformRepository.delete(platform);

        return ResponseEntity.noContent().build();
    }
}