package com.agendaedu.educacional.Entities;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "codigo_acesso", nullable = false, unique = true)
    private String codigoAcesso;

    // Um professor pode ter várias salas 
    @ManyToOne
    @JoinColumn(name = "professor_id")
    private User professor;

    @OneToMany(mappedBy = "sala")
    private List<User> alunos;
}
