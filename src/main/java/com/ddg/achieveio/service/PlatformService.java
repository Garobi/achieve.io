package com.ddg.achieveio.service;

import com.ddg.achieveio.dto.request.PlatformRequestDTO;
import com.ddg.achieveio.dto.response.PlatformResponseDTO;
import com.ddg.achieveio.entity.Platform;
import com.ddg.achieveio.repository.PlatformRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PlatformService {

    private final PlatformRepository platformRepository;

    public PlatformService(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    public PlatformResponseDTO createPlatform(PlatformRequestDTO requestDTO) {
        Platform newPlatform = new Platform();
        newPlatform.setPlatformName(requestDTO.platformName());
        newPlatform.setPlatformDescription(requestDTO.platformDescription());
        newPlatform.setImageUrl(requestDTO.imageUrl());

        Platform savedPlatform = platformRepository.save(newPlatform);

        return new PlatformResponseDTO(savedPlatform);
    }

    public List<PlatformResponseDTO> getAllPlatforms() {
        List<Platform> platforms = platformRepository.findAll();

        return platforms.stream()
                .map(PlatformResponseDTO::new)
                .collect(Collectors.toList());
    }

    public PlatformResponseDTO getPlatformById(UUID id) {
        Platform platform = platformRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plataforma não encontrada com id: " + id));

        return new PlatformResponseDTO(platform);
    }

    public PlatformResponseDTO updatePlatform(UUID id, PlatformRequestDTO requestDTO) {
        Platform platform = platformRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plataforma não encontrada com id: " + id));

        platform.setPlatformName(requestDTO.platformName());
        platform.setPlatformDescription(requestDTO.platformDescription());
        platform.setImageUrl(requestDTO.imageUrl());

        Platform updatedPlatform = platformRepository.save(platform);

        return new PlatformResponseDTO(updatedPlatform);
    }

    public void deletePlatform(UUID id) {
        Platform platform = platformRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plataforma não encontrada com id: " + id));

        platformRepository.delete(platform);
    }
}