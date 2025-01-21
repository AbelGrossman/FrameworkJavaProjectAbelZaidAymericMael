package fr.pantheonsorbonne.ufr27.miage.dto;

import fr.pantheonsorbonne.ufr27.miage.model.Theme;

public record UserDto(
        Long userId,
        String theme,
        Integer mmr
) {}
