// ActivityDTO.java
package com.agendaedu.educacional.DTOs.atividade;

import java.time.LocalDateTime;

public record ActivityDTO(
    Long id,
    String titulo,
    String descricao,
    String local,
    LocalDateTime data
) {}
