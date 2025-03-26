package com.agendaedu.educacional.DTOs.usuario;

import com.agendaedu.educacional.Enums.Role;
import com.agendaedu.educacional.Entities.usuario.User;

public record SimpleUserDTO(
    Long id,
    String nome,
    Role role
) {
    public SimpleUserDTO(User user) {
    this(user.getId(), user.getNomeCompleto(), user.getRole());
    }
}