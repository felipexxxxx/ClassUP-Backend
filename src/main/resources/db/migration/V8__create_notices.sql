CREATE TABLE notices (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    mensagem TEXT NOT NULL,
    enviada_em TIMESTAMP NOT NULL,
    sala_id BIGINT,
    FOREIGN KEY (sala_id) REFERENCES classes(id)
);
