package com.ddg.achieveio.controller;

import com.ddg.achieveio.dto.request.PlatformRequestDTO;
import com.ddg.achieveio.dto.response.PlatformResponseDTO;
import com.ddg.achieveio.service.PlatformService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/platforms")
public class PlatformController {

    private final PlatformService platformService;

    public PlatformController(PlatformService platformService) {
        this.platformService = platformService;
    }

    @PostMapping
    public ResponseEntity<PlatformResponseDTO> createPlatform(
            @Valid @RequestBody PlatformRequestDTO requestDTO
    ) {
        PlatformResponseDTO responseDTO = platformService.createPlatform(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<PlatformResponseDTO>> getAllPlatforms() {
        List<PlatformResponseDTO> responseList = platformService.getAllPlatforms();

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatformResponseDTO> getPlatformById(@PathVariable UUID id) {
        PlatformResponseDTO responseDTO = platformService.getPlatformById(id);

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlatformResponseDTO> updatePlatform(
            @PathVariable UUID id,
            @Valid @RequestBody PlatformRequestDTO requestDTO
    ) {
        PlatformResponseDTO responseDTO = platformService.updatePlatform(id, requestDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlatform(@PathVariable UUID id) {
        platformService.deletePlatform(id);

        return ResponseEntity.noContent().build();
    }
}