package com.agendaedu.educacional.DTOs.atividade;

import java.time.LocalDateTime;

import com.agendaedu.educacional.Enums.PresenceStatus;

public record StudentActivityDTO (
     Long id,
    String titulo,
    String descricao,
    LocalDateTime data,
    PresenceStatus status,
    String local
) {}

