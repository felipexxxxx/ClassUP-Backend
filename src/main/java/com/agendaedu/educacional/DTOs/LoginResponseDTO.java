package com.agendaedu.educacional.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {
    private String mensagem;
    private String accessToken;
    private String role; 
}
