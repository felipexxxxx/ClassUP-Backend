package com.agendaedu.educacional.DTOs.aviso;

import com.agendaedu.educacional.Entities.sala.ClassEntity;

import lombok.Data;

@Data
public class NoticeDTO {
    private String titulo;
    private String mensagem;
    private ClassEntity sala;
}
