package com.agendaedu.educacional.DTOs;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class ClassHistoryDetalhesDTO {
    private String nomeSala;
    private String codigoAcesso;
    private LocalDateTime dataEncerramento;
    private SimpleUserDTO professor;
    private List<SimpleUserDTO> alunos;
    private List<ActivityHistoryDTO> atividades;
    private List<NoticeHistoryDTO> avisos;
}


