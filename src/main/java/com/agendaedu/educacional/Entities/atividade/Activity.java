package com.agendaedu.educacional.Entities.atividade;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.agendaedu.educacional.Entities.sala.ClassEntity;


@Getter
@Setter
@Entity
@Table(name = "activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String descricao;

    private String local;

    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = false)
    private ClassEntity sala;
    }