package com.agendaedu.educacional.DTOs;

import lombok.AllArgsConstructor;
import com.agendaedu.educacional.Entities.PresenceStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ActivityHistoryDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime data;
    private PresenceStatus status;
    private String local;
}
