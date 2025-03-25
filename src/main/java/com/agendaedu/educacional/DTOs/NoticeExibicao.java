package com.agendaedu.educacional.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NoticeExibicao {
    private Long id;
    private String titulo;
    private String mensagem;
    private LocalDateTime enviadaEm;
}
