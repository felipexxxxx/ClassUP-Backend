package com.agendaedu.educacional.DTOs;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

import com.agendaedu.educacional.Entities.enums.PresenceStatus;

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
