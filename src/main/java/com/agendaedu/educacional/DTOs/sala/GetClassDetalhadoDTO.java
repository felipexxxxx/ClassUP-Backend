// DTOs/GetClassDetalhadoDTO.java
package com.agendaedu.educacional.DTOs.sala;


import com.agendaedu.educacional.DTOs.atividade.ActivityDTO;
import com.agendaedu.educacional.Entities.aviso.Notice;
import com.agendaedu.educacional.Entities.usuario.User;

import java.util.List;

public record GetClassDetalhadoDTO(
    Long id,
    String nome,
    String codigoAcesso,
    User professor,
    List<User> alunos,
    List<ActivityDTO> atividades, 
    List<Notice> avisos        
) {}

