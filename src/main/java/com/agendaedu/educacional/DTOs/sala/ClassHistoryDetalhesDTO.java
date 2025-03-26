package com.agendaedu.educacional.DTOs.sala;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import com.agendaedu.educacional.DTOs.atividade.*;
import com.agendaedu.educacional.DTOs.aviso.*;
import com.agendaedu.educacional.DTOs.usuario.*;

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
    private List<NoticeExibicaoDTO> avisos;
}


