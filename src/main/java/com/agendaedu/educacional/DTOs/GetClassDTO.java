package com.agendaedu.educacional.DTOs;
import java.util.List;


public record GetClassDTO(
    Long id,
    String nome,
    SimpleUserDTO professor,
    List<SimpleUserDTO> alunos
) {}
