package com.agendaedu.educacional.DTOs;

public record UpdatePasswordDTO(
    String senhaAtual,
    String novaSenha
) {}
