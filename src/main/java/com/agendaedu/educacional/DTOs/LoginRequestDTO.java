package com.agendaedu.educacional.DTOs;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class LoginRequestDTO {
    private String email;
    private String matricula;
    private String senha;
}
