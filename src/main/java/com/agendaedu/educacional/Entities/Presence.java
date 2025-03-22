package com.agendaedu.educacional.Entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "presence", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usuario_id", "atividade_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Presence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PresenceStatus status;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "atividade_id", nullable = false)
    private Activity atividade;
}
