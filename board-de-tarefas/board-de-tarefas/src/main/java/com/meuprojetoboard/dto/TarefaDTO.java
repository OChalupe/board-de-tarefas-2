package com.meuprojetoboard.dto;

import com.meuprojetoboard.model.StatusTarefa;
import com.meuprojetoboard.model.Tarefa;

import java.time.LocalDateTime;

public class TarefaDTO {
    private String id;
    private String titulo;
    private String descricao;
    private StatusTarefa status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    // Construtor para criar DTO a partir de nova entrada (ID e datas podem ser nulos)
    public TarefaDTO(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
        // O status inicial será definido na camada de serviço/entidade
    }

    // Construtor para criar DTO a partir de uma Tarefa (entidade)
    public TarefaDTO(Tarefa tarefa) {
        this.id = tarefa.getId();
        this.titulo = tarefa.getTitulo();
        this.descricao = tarefa.getDescricao();
        this.status = tarefa.getStatus();
        this.dataCriacao = tarefa.getDataCriacao();
        this.dataAtualizacao = tarefa.getDataAtualizacao();
    }

    // Construtor completo para casos de atualização onde o ID é conhecido
    public TarefaDTO(String id, String titulo, String descricao, StatusTarefa status, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    // --- Getters e Setters (para os campos que podem ser modificados ou lidos pela UI) ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public StatusTarefa getStatus() { return status; }
    public void setStatus(StatusTarefa status) { this.status = status; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }

    @Override
    public String toString() {
        return "ID: " + (id != null ? id : "N/A") +
               "\nTítulo: " + titulo +
               "\nDescrição: " + (descricao != null && !descricao.isEmpty() ? descricao : "[Sem Descrição]") +
               "\nStatus: " + (status != null ? status.getDescricao() : "N/A") +
               "\nCriada em: " + (dataCriacao != null ? dataCriacao.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : "N/A") +
               "\nÚltima Atualização: " + (dataAtualizacao != null ? dataAtualizacao.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : "N/A");
    }
}