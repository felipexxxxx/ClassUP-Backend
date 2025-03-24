package com.agendaedu.educacional.DTOs;

import lombok.AllArgsConstructor;
import java.time.LocalDateTime;


@AllArgsConstructor
public class ActivityHistoryDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime data;
}
