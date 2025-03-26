package com.agendaedu.educacional.DTOs;

import java.time.LocalDateTime;

public record ActivityResumoDTO(
    Long id,
    String titulo,
    String descricao,
    LocalDateTime data,
    String local,
    int confirmados
) {}
