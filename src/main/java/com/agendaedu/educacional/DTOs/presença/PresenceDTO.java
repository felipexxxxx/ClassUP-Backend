package com.agendaedu.educacional.DTOs.presença;

public record PresenceDTO(
        Long userId,
        Long activityId,
        boolean presente
) {}
