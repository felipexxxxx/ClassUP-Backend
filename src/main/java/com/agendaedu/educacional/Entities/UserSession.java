package com.agendaedu.educacional.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Alterei de OneToOne para ManyToOne (um usuário pode ter várias sessões)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;

    @Column(name = "entrou", nullable = false)
    private LocalDateTime entrou;

    @Column(name = "saiu")
    private LocalDateTime saiu;
}
