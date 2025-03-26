package com.agendaedu.educacional.DTOs.usuario;

public record UpdatePasswordDTO(
    String senhaAtual,
    String novaSenha
) {}
