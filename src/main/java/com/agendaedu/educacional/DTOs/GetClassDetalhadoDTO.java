// DTOs/GetClassDetalhadoDTO.java
package com.agendaedu.educacional.DTOs;

import com.agendaedu.educacional.Entities.Activity;
import com.agendaedu.educacional.Entities.Notice;
import com.agendaedu.educacional.Entities.User;

import java.util.List;

public record GetClassDetalhadoDTO(
    Long id,
    String nome,
    String codigoAcesso,
    User professor,
    List<User> alunos,
    List<Activity> atividades,
    List<Notice> avisos
) {}
