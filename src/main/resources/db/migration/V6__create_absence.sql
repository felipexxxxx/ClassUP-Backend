CREATE TABLE absence (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    
    aluno_id BIGINT NOT NULL,
    atividade_id BIGINT NOT NULL,

    confirmou_presenca BOOLEAN NOT NULL DEFAULT FALSE,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (aluno_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (atividade_id) REFERENCES activities(id) ON DELETE CASCADE,

    UNIQUE (aluno_id, atividade_id) -- Garante que cada aluno tenha uma única presença por atividade
);
