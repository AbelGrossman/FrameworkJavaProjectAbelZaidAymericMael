package fr.pantheonsorbonne.ufr27.miage.dto;

import fr.pantheonsorbonne.ufr27.miage.model.Theme;

import java.util.List;

public record TeamResponseDto(
        String teamId,
        String theme,
        List<UserDto> players,
        String difficulty
) {}