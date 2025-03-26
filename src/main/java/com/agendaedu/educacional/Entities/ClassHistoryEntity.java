package com.agendaedu.educacional.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.agendaedu.educacional.Entities.enums.Role;

@Entity
@Table(name = "classes_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = false)
    private ClassEntity sala;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "data_encerramento")
    private LocalDateTime dataEncerramento;
}
