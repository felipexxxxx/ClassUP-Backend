package com.agendaedu.educacional.DTOs;

import java.time.LocalDateTime;

public record NotificationDTO(
        String mensagem,
        LocalDateTime dataEnvio,
        Long classId,
        Long activityId
) {}
