package com.agendaedu.educacional.DTOs;

import java.time.LocalDateTime;

import com.agendaedu.educacional.Entities.PresenceStatus;

public record StudentActivityDTO (
     Long id,
    String titulo,
    String descricao,
    LocalDateTime data,
    PresenceStatus status,
    String local
) {}

