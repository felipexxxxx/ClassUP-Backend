CREATE TABLE activities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    local VARCHAR(100),
    data_hora DATETIME NOT NULL,
    sala_id BIGINT NOT NULL,
    FOREIGN KEY (sala_id) REFERENCES classes(id) ON DELETE CASCADE
);
