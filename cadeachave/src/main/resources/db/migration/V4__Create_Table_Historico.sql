CREATE TABLE IF NOT EXISTS historico (
    id SERIAL PRIMARY KEY,
    professores_salas_id INT NOT NULL REFERENCES professores_salas(id),
    horario TIMESTAMP NOT NULL,
    acao CHAR(1) NOT NULL
);