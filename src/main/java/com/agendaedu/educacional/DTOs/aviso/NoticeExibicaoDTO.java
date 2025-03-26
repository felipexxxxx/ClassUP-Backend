package com.agendaedu.educacional.DTOs.aviso;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NoticeExibicaoDTO {
    private Long id;
    private String titulo;
    private String mensagem;
    private LocalDateTime enviadaEm;
}
