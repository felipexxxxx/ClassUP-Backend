package com.agendaedu.educacional.Entities.aviso;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.agendaedu.educacional.Entities.sala.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "notices") 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String mensagem;

    @Column(name = "enviada_em")
    private LocalDateTime enviadaEm; 

    @ManyToOne
    @JoinColumn(name = "classroom_id") 
    @JsonIgnore 
    private ClassEntity sala;
}
