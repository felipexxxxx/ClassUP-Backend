CREATE TABLE presence (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    status ENUM('PENDENTE', 'CONFIRMADO', 'RECUSADO') NOT NULL DEFAULT 'PENDENTE',

    usuario_id BIGINT NOT NULL,
    atividade_id BIGINT NOT NULL,

    FOREIGN KEY (usuario_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (atividade_id) REFERENCES activities(id) ON DELETE CASCADE,

    UNIQUE (usuario_id, atividade_id)
);
