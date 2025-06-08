package com.meuprojetoboard.dao.impl;

import com.meuprojetoboard.dao.TarefaDAO;
import com.meuprojetoboard.model.StatusTarefa;
import com.meuprojetoboard.model.Tarefa;
import com.meuprojetoboard.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TarefaDAOJdbcImpl implements TarefaDAO {

    @Override
    public Tarefa salvar(Tarefa tarefa) {
        String sql;
        // Verifica se a tarefa já existe para decidir entre INSERT e UPDATE
        if (buscarPorId(tarefa.getId()).isPresent()) {
            // É um UPDATE
            sql = "UPDATE tarefas SET titulo = ?, descricao = ?, status = ?, data_atualizacao = ? WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, tarefa.getTitulo());
                stmt.setString(2, tarefa.getDescricao());
                stmt.setString(3, tarefa.getStatus().name());
                stmt.setTimestamp(4, Timestamp.valueOf(tarefa.getDataAtualizacao()));
                stmt.setString(5, tarefa.getId());
                stmt.executeUpdate();
                System.out.println("Tarefa atualizada com sucesso: " + tarefa.getId());
            } catch (SQLException e) {
                System.err.println("Erro ao atualizar tarefa: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Erro de persistência ao atualizar tarefa.", e);
            }
        } else {
            // É um INSERT
            sql = "INSERT INTO tarefas (id, titulo, descricao, status, data_criacao, data_atualizacao) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, tarefa.getId());
                stmt.setString(2, tarefa.getTitulo());
                stmt.setString(3, tarefa.getDescricao());
                stmt.setString(4, tarefa.getStatus().name());
                stmt.setTimestamp(5, Timestamp.valueOf(tarefa.getDataCriacao()));
                stmt.setTimestamp(6, Timestamp.valueOf(tarefa.getDataAtualizacao()));
                stmt.executeUpdate();
                System.out.println("Tarefa salva com sucesso: " + tarefa.getId());
            } catch (SQLException e) {
                System.err.println("Erro ao salvar nova tarefa: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Erro de persistência ao salvar tarefa.", e);
            }
        }
        return tarefa;
    }

    @Override
    public Optional<Tarefa> buscarPorId(String id) {
        String sql = "SELECT id, titulo, descricao, status, data_criacao, data_atualizacao FROM tarefas WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultSetParaTarefa(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar tarefa por ID: " + e.getMessage());
            e.printStackTrace();
            // A exceção pode ser relançada ou tratada de forma mais específica
        }
        return Optional.empty();
    }

    @Override
    public List<Tarefa> buscarTodos() {
        List<Tarefa> tarefas = new ArrayList<>();
        String sql = "SELECT id, titulo, descricao, status, data_criacao, data_atualizacao FROM tarefas ORDER BY data_criacao DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tarefas.add(mapearResultSetParaTarefa(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todas as tarefas: " + e.getMessage());
            e.printStackTrace();
        }
        return tarefas;
    }

    @Override
    public List<Tarefa> buscarPorStatus(StatusTarefa status) {
        List<Tarefa> tarefas = new ArrayList<>();
        String sql = "SELECT id, titulo, descricao, status, data_criacao, data_atualizacao FROM tarefas WHERE status = ? ORDER BY data_criacao DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tarefas.add(mapearResultSetParaTarefa(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar tarefas por status: " + e.getMessage());
            e.printStackTrace();
        }
        return tarefas;
    }

    @Override
    public boolean deletar(String id) {
        String sql = "DELETE FROM tarefas WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar tarefa: " + e.getMessage());
            e.printStackTrace();
            // A exceção pode ser relançada ou tratada de forma mais específica
        }
        return false;
    }

    // Método utilitário para mapear ResultSet para objeto Tarefa
    private Tarefa mapearResultSetParaTarefa(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String titulo = rs.getString("titulo");
        String descricao = rs.getString("descricao");
        StatusTarefa status = StatusTarefa.valueOf(rs.getString("status"));
        LocalDateTime dataCriacao = rs.getTimestamp("data_criacao").toLocalDateTime();
        LocalDateTime dataAtualizacao = rs.getTimestamp("data_atualizacao").toLocalDateTime();
        return new Tarefa(id, titulo, descricao, status, dataCriacao, dataAtualizacao);
    }
}