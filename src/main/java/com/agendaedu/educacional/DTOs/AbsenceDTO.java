package com.agendaedu.educacional.DTOs;

public record AbsenceDTO(
        Long userId,
        Long activityId,
        boolean presente
) {}
