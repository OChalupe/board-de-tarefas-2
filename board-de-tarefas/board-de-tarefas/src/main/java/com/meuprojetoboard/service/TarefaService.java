package com.meuprojetoboard.service;

import com.meuprojetoboard.dao.TarefaDAO;
import com.meuprojetoboard.dao.impl.TarefaDAOJdbcImpl; // Importe a implementação concreta
import com.meuprojetoboard.dto.TarefaDTO;
import com.meuprojetoboard.model.StatusTarefa;
import com.meuprojetoboard.model.Tarefa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TarefaService {
    private TarefaDAO tarefaDAO;

    public TarefaService() {
        // Injeção de dependência manual (para projetos maiores, usar Frameworks como Spring)
        this.tarefaDAO = new TarefaDAOJdbcImpl();
    }

    // Usamos DTOs para entrada da UI
    public TarefaDTO criarTarefa(TarefaDTO tarefaDTO) {
        if (tarefaDTO.getTitulo() == null || tarefaDTO.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("O título da tarefa não pode ser vazio.");
        }
        // Converte DTO para Entidade para persistir
        Tarefa novaTarefa = new Tarefa(tarefaDTO.getTitulo(), tarefaDTO.getDescricao());
        Tarefa tarefaSalva = tarefaDAO.salvar(novaTarefa);
        return new TarefaDTO(tarefaSalva); // Retorna DTO da tarefa salva
    }

    public List<TarefaDTO> listarTodasTarefas() {
        return tarefaDAO.buscarTodos().stream()
                .map(TarefaDTO::new) // Converte cada Tarefa (entidade) para TarefaDTO
                .collect(Collectors.toList());
    }

    public Optional<TarefaDTO> buscarTarefaPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty(); // IDs inválidos não devem retornar resultados
        }
        return tarefaDAO.buscarPorId(id)
                .map(TarefaDTO::new); // Converte Tarefa (entidade) para TarefaDTO
    }

    public boolean atualizarTarefa(TarefaDTO tarefaDTO) {
        if (tarefaDTO.getId() == null || tarefaDTO.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("ID da tarefa é necessário para atualização.");
        }
        Optional<Tarefa> tarefaExistenteOpt = tarefaDAO.buscarPorId(tarefaDTO.getId());
        if (tarefaExistenteOpt.isPresent()) {
            Tarefa tarefaExistente = tarefaExistenteOpt.get();

            // Atualiza campos da entidade com base no DTO
            if (tarefaDTO.getTitulo() != null && !tarefaDTO.getTitulo().trim().isEmpty()) {
                tarefaExistente.setTitulo(tarefaDTO.getTitulo());
            } else {
                // Se o título vier vazio no DTO de atualização, mantemos o existente.
                // Outra abordagem seria lançar exceção se for um campo obrigatório.
            }

            if (tarefaDTO.getDescricao() != null) {
                tarefaExistente.setDescricao(tarefaDTO.getDescricao());
            }

            if (tarefaDTO.getStatus() != null) {
                tarefaExistente.setStatus(tarefaDTO.getStatus());
            }

            tarefaDAO.salvar(tarefaExistente); // O método salvar já faz o update
            return true;
        }
        return false; // Tarefa não encontrada
    }

    public boolean removerTarefa(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID da tarefa não pode ser vazio para remoção.");
        }
        return tarefaDAO.deletar(id);
    }

    public List<TarefaDTO> buscarTarefasPorStatus(StatusTarefa status) {
        if (status == null) {
            throw new IllegalArgumentException("Status não pode ser nulo para busca.");
        }
        return tarefaDAO.buscarPorStatus(status).stream()
                .map(TarefaDTO::new)
                .collect(Collectors.toList());
    }
}