package com.meuprojetoboard.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String PROPERTIES_FILE = "database.properties";
    private static Properties properties = new Properties();

    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                System.err.println("Sorry, unable to find " + PROPERTIES_FILE);
            }
            properties.load(input);
            // Carrega o driver JDBC (necessário para versões mais antigas do JDBC, mas boa prática)
            Class.forName("org.h2.Driver");
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Erro ao carregar configurações do banco de dados ou driver.", ex);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }

    public static void initializeDatabase() {
        // Método para executar o script de criação da tabela na primeira vez
        try (Connection conn = getConnection();
             java.sql.Statement stmt = conn.createStatement()) {

            String createTableSql = "CREATE TABLE IF NOT EXISTS tarefas (" +
                                    "id VARCHAR(36) PRIMARY KEY," + // Mude para UUID se o banco suportar
                                    "titulo VARCHAR(255) NOT NULL," +
                                    "descricao VARCHAR(1000)," +
                                    "status VARCHAR(50) NOT NULL," +
                                    "data_criacao TIMESTAMP NOT NULL," +
                                    "data_atualizacao TIMESTAMP NOT NULL" +
                                    ");";
            stmt.execute(createTableSql);
            System.out.println("Tabela 'tarefas' verificada/criada com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}