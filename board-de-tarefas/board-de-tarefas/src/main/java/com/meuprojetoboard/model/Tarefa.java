package com.meuprojetoboard.model;

import java.time.LocalDateTime;
import java.util.UUID; // Para gerar IDs únicos

public class Tarefa {
    private String id; // Usaremos String para UUIDs
    private String titulo;
    private String descricao;
    private StatusTarefa status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    // Construtor para novas tarefas (ID e datas gerados automaticamente)
    public Tarefa(String titulo, String descricao) {
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = StatusTarefa.A_FAZER;
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    // Construtor para carregar tarefas do banco (todos os campos definidos)
    public Tarefa(String id, String titulo, String descricao, StatusTarefa status, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    // --- Getters e Setters ---
    public String getId() { return id; }
    // Setter de ID apenas se precisar atribuir um ID externo, caso contrário, não é necessário.
    public void setId(String id) { this.id = id; } // Adicionado para uso no DAO ao carregar

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; this.dataAtualizacao = LocalDateTime.now(); }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; this.dataAtualizacao = LocalDateTime.now(); }

    public StatusTarefa getStatus() { return status; }
    public void setStatus(StatusTarefa status) { this.status = status; this.dataAtualizacao = LocalDateTime.now(); }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    // Setter de dataCriacao não é comum, pois é a data original.
    // public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    // Setter de dataAtualizacao é manipulado pelos outros setters ou pelo DAO ao salvar.
    // public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }


    @Override
    public String toString() {
        return "ID: " + id +
               "\nTítulo: " + titulo +
               "\nDescrição: " + (descricao != null && !descricao.isEmpty() ? descricao : "[Sem Descrição]") +
               "\nStatus: " + status.getDescricao() +
               "\nCriada em: " + dataCriacao.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
               "\nÚltima Atualização: " + dataAtualizacao.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}