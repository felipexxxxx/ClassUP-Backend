package com.agendaedu.educacional.DTOs;

public record PresenceDTO(
        Long userId,
        Long activityId,
        boolean presente
) {}
