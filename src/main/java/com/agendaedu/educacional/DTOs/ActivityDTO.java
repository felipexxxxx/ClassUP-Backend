// ActivityDTO.java
package com.agendaedu.educacional.DTOs;

import java.time.LocalDateTime;

public record ActivityDTO(
    Long id,
    String titulo,
    String descricao,
    String local,
    LocalDateTime dataHora
) {}
