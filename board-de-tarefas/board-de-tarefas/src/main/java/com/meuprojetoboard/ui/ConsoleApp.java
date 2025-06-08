package com.meuprojetoboard.ui;

import com.meuprojetoboard.dto.TarefaDTO;
import com.meuprojetoboard.model.StatusTarefa;
import com.meuprojetoboard.service.TarefaService;
import com.meuprojetoboard.util.DatabaseConnection;// Para inicializar o DB

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleApp {
    private TarefaService tarefaService;
    private Scanner scanner;

    public ConsoleApp() {
        this.tarefaService = new TarefaService(); // Instancia o Service
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        // Inicializa o banco de dados e cria a tabela se não existir
        DatabaseConnection.initializeDatabase();

        int opcao;
        do {
            exibirMenu();
            opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    criarTarefa();
                    break;
                case 2:
                    listarTodasTarefas();
                    break;
                case 3:
                    buscarTarefaPorId();
                    break;
                case 4:
                    atualizarTarefa();
                    break;
                case 5:
                    removerTarefa();
                    break;
                case 6:
                    listarTarefasPorStatus();
                    break;
                case 0:
                    System.out.println("Saindo do aplicativo. Até mais!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine(); // Consome a nova linha após a opção e espera por Enter
        } while (opcao != 0);
        scanner.close(); // Fecha o scanner ao sair
    }

    private void exibirMenu() {
        System.out.println("\n--- Board de Tarefas ---");
        System.out.println("1. Criar nova tarefa");
        System.out.println("2. Listar todas as tarefas");
        System.out.println("3. Ver detalhes de uma tarefa");
        System.out.println("4. Atualizar tarefa");
        System.out.println("5. Remover tarefa");
        System.out.println("6. Listar tarefas por status");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private int lerOpcao() {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.next(); // Consome a entrada inválida
            System.out.print("Escolha uma opção: ");
        }
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Consome a nova linha
        return opcao;
    }

    private void criarTarefa() {
        System.out.println("\n--- Criar Nova Tarefa ---");
        System.out.print("Título da Tarefa: ");
        String titulo = scanner.nextLine();
        System.out.print("Descrição da Tarefa: ");
        String descricao = scanner.nextLine();

        try {
            TarefaDTO novaTarefaDTO = new TarefaDTO(titulo, descricao);
            TarefaDTO tarefaCriada = tarefaService.criarTarefa(novaTarefaDTO);
            System.out.println("Tarefa criada com sucesso!");
            System.out.println(tarefaCriada);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao criar tarefa: " + e.getMessage());
        } catch (RuntimeException e) { // Captura exceções da camada de persistência
            System.err.println("Ocorreu um erro inesperado ao salvar a tarefa. Detalhes: " + e.getMessage());
        }
    }

    private void listarTodasTarefas() {
        System.out.println("\n--- Todas as Tarefas ---");
        List<TarefaDTO> tarefas = tarefaService.listarTodasTarefas();
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada.");
        } else {
            tarefas.forEach(t -> System.out.println("- " + t.getTitulo() + " (Status: " + t.getStatus().getDescricao() + ") [ID: " + t.getId().substring(0, Math.min(t.getId().length(), 8)) + "...]"));
        }
    }

    private void buscarTarefaPorId() {
        System.out.println("\n--- Detalhes da Tarefa ---");
        System.out.print("Digite o ID da tarefa: ");
        String id = scanner.nextLine();

        try {
            Optional<TarefaDTO> tarefaOpt = tarefaService.buscarTarefaPorId(id);
            if (tarefaOpt.isPresent()) {
                System.out.println(tarefaOpt.get());
            } else {
                System.out.println("Tarefa com ID '" + id + "' não encontrada.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao buscar tarefa: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Ocorreu um erro inesperado ao buscar a tarefa. Detalhes: " + e.getMessage());
        }
    }

    private void atualizarTarefa() {
        System.out.println("\n--- Atualizar Tarefa ---");
        System.out.print("Digite o ID da tarefa a ser atualizada: ");
        String id = scanner.nextLine();

        try {
            Optional<TarefaDTO> tarefaOpt = tarefaService.buscarTarefaPorId(id);
            if (tarefaOpt.isPresent()) {
                TarefaDTO tarefaAtual = tarefaOpt.get();
                System.out.println("Tarefa encontrada: " + tarefaAtual.getTitulo());

                System.out.print("Novo Título (deixe em branco para manter '" + tarefaAtual.getTitulo() + "'): ");
                String novoTitulo = scanner.nextLine();
                // Se o novo título for vazio, mantenha o antigo no DTO para atualização
                novoTitulo = novoTitulo.isEmpty() ? tarefaAtual.getTitulo() : novoTitulo;


                System.out.print("Nova Descrição (deixe em branco para manter a atual): ");
                String novaDescricao = scanner.nextLine();
                novaDescricao = novaDescricao.isEmpty() ? tarefaAtual.getDescricao() : novaDescricao;


                System.out.println("Status atuais disponíveis:");
                for (StatusTarefa s : StatusTarefa.values()) {
                    System.out.println(s.ordinal() + " - " + s.getDescricao());
                }
                System.out.print("Novo Status (digite o número correspondente, deixe em branco para manter '" + tarefaAtual.getStatus().getDescricao() + "'): ");
                String statusInput = scanner.nextLine();
                StatusTarefa novoStatus = tarefaAtual.getStatus(); // Mantém o status atual por padrão
                if (!statusInput.isEmpty()) {
                    try {
                        int statusOrdinal = Integer.parseInt(statusInput);
                        if (statusOrdinal >= 0 && statusOrdinal < StatusTarefa.values().length) {
                            novoStatus = StatusTarefa.values()[statusOrdinal];
                        } else {
                            System.out.println("Número de status inválido. Mantendo o status atual.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada de status inválida. Mantendo o status atual.");
                    }
                }

                // Cria um DTO para enviar ao serviço com os dados atualizados
                TarefaDTO tarefaParaAtualizar = new TarefaDTO(
                    id, // ID é crucial para atualização
                    novoTitulo,
                    novaDescricao,
                    novoStatus,
                    tarefaAtual.getDataCriacao(), // Mantém as datas de criação e atualização no DTO
                    tarefaAtual.getDataAtualizacao()
                );
                tarefaParaAtualizar.setStatus(novoStatus); // Garante que o status seja setado

                if (tarefaService.atualizarTarefa(tarefaParaAtualizar)) {
                    System.out.println("Tarefa atualizada com sucesso!");
                } else {
                    System.out.println("Não foi possível atualizar a tarefa. ID pode estar incorreto ou erro interno.");
                }
            } else {
                System.out.println("Tarefa com ID '" + id + "' não encontrada.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao atualizar tarefa: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Ocorreu um erro inesperado ao atualizar a tarefa. Detalhes: " + e.getMessage());
        }
    }

    private void removerTarefa() {
        System.out.println("\n--- Remover Tarefa ---");
        System.out.print("Digite o ID da tarefa a ser removida: ");
        String id = scanner.nextLine();

        try {
            if (tarefaService.removerTarefa(id)) {
                System.out.println("Tarefa removida com sucesso!");
            } else {
                System.out.println("Tarefa com ID '" + id + "' não encontrada ou não pôde ser removida.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao remover tarefa: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Ocorreu um erro inesperado ao remover a tarefa. Detalhes: " + e.getMessage());
        }
    }

    private void listarTarefasPorStatus() {
        System.out.println("\n--- Listar Tarefas por Status ---");
        System.out.println("Escolha um status:");
        for (StatusTarefa s : StatusTarefa.values()) {
            System.out.println(s.ordinal() + " - " + s.getDescricao());
        }
        System.out.print("Digite o número do status: ");
        String statusInput = scanner.nextLine();

        try {
            int statusOrdinal = Integer.parseInt(statusInput);
            if (statusOrdinal >= 0 && statusOrdinal < StatusTarefa.values().length) {
                StatusTarefa statusSelecionado = StatusTarefa.values()[statusOrdinal];
                List<TarefaDTO> tarefas = tarefaService.buscarTarefasPorStatus(statusSelecionado);

                if (tarefas.isEmpty()) {
                    System.out.println("Nenhuma tarefa com o status '" + statusSelecionado.getDescricao() + "' encontrada.");
                } else {
                    System.out.println("\n--- Tarefas com status: " + statusSelecionado.getDescricao() + " ---");
                    tarefas.forEach(t -> System.out.println("- " + t.getTitulo() + " [ID: " + t.getId().substring(0, Math.min(t.getId().length(), 8)) + "...]"));
                }
            } else {
                System.out.println("Número de status inválido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao listar tarefas por status: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Ocorreu um erro inesperado ao listar as tarefas. Detalhes: " + e.getMessage());
        }
    }
}