CREATE TABLE IF NOT EXISTS professores_salas (
    id SERIAL PRIMARY KEY,
    professor_id INTEGER REFERENCES professores(id),
    sala_id INTEGER REFERENCES salas(id)
);