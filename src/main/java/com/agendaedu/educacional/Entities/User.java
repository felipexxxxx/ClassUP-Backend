package com.agendaedu.educacional.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import com.agendaedu.educacional.Entities.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    @Column(nullable = false, unique = true)
    private String matricula;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Relacionamento com sala (aluno participa de uma sala)
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "sala_id")
    private ClassEntity sala; }


