package com.agendaedu.educacional.DTOs;

import java.time.LocalDateTime;

public record ActivityDTO(
        String titulo,
        String descricao,
        String local,
        LocalDateTime dataHora,
        Long classId
) {}
