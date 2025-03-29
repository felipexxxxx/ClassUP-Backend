package com.agendaedu.educacional.DTOs.admin;

public record UsuarioImportadoDTO(
    String nomeCompleto,
    String email,
    String cpf,
    String role,
    String dataNascimento
) {}
