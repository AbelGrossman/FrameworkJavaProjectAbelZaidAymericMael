package fr.pantheonsorbonne.ufr27.miage.dto;

import java.util.List;

public record TeamResponseDto(
        String id,
        String theme,
        List<Long> players,
        String difficulty
) {}