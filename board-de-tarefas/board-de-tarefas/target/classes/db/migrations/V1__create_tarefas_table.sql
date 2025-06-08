-- V1__create_tarefas_table.sql
CREATE TABLE IF NOT EXISTS tarefas (
    id UUID PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao VARCHAR(1000),
    status VARCHAR(50) NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    data_atualizacao TIMESTAMP NOT NULL
);