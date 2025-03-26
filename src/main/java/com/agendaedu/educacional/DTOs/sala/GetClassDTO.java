package com.agendaedu.educacional.DTOs.sala;
import java.util.List;

import com.agendaedu.educacional.DTOs.usuario.SimpleUserDTO;


public record GetClassDTO(
    Long id,
    String nome,
    SimpleUserDTO professor,
    List<SimpleUserDTO> alunos
) {}
