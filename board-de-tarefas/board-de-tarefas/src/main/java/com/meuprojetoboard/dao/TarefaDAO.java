package com.meuprojetoboard.dao;

import com.meuprojetoboard.model.StatusTarefa;
import com.meuprojetoboard.model.Tarefa;

import java.util.List;
import java.util.Optional;

// Interface define o CONTRATO para acesso a dados de Tarefas
public interface TarefaDAO {
    Tarefa salvar(Tarefa tarefa); // Salva uma nova tarefa ou atualiza uma existente
    Optional<Tarefa> buscarPorId(String id);
    List<Tarefa> buscarTodos();
    List<Tarefa> buscarPorStatus(StatusTarefa status);
    boolean deletar(String id);
    // O método 'salvar' será polivalente para insert/update, ou podemos ter 'inserir' e 'atualizar' separados
    // boolena atualizar(Tarefa tarefa); // Se preferir um método de update explícito
}