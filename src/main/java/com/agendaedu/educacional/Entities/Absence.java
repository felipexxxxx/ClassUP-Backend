package com.agendaedu.educacional.Entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "absence", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usuario_id", "atividade_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Absence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean presente;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "atividade_id", nullable = false)
    private Activity atividade;
}
