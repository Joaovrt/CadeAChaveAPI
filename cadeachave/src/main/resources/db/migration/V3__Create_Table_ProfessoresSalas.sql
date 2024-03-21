CREATE TABLE IF NOT EXISTS professores_salas (
    professor_id INTEGER REFERENCES professores(id),
    sala_id INTEGER REFERENCES salas(id),
    PRIMARY KEY (professor_id, sala_id)
);