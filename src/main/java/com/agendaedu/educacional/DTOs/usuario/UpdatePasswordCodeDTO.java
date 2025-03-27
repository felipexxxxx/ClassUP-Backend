package com.agendaedu.educacional.DTOs.usuario;

public record UpdatePasswordCodeDTO (    
String novaSenha,
String codigoAutenticacao,
String email)
{}

