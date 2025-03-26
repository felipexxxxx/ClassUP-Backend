package com.agendaedu.educacional.DTOs.presenca;

public record PresenceDTO(
        Long userId,
        Long activityId,
        boolean presente
) {}
