CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    atividade_id BIGINT NOT NULL,
    enviada BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (usuario_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (atividade_id) REFERENCES activities(id) ON DELETE CASCADE
);