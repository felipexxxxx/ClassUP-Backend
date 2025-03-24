package com.agendaedu.educacional.DTOs;

import com.agendaedu.educacional.Entities.ClassEntity;
import lombok.Data;

@Data
public class NoticeDTO {
    private String titulo;
    private String mensagem;
    private ClassEntity sala;
}
