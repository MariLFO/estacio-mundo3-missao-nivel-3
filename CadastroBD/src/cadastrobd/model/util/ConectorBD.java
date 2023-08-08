package cadastrobd.model.util;

/**
 *
 * @author Mari
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConectorBD {
    private static final String DRIVER = ""; // Nome do driver JDBC
    private static final String URL = ""; // URL do banco de dados
    private static final String USER = ""; // Usuário do banco de dados
    private static final String PASSWORD = ""; // Senha do banco de dados

    // Método getConnection
    public static Connection getConnection() {
        try {
            // Carrega o driver JDBC na memória
            Class.forName(DRIVER);
            // Retorna uma conexão com o banco de dados
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Erro ao conectar com o banco de dados: " + e.getMessage());
            return null;
        }
    }

    public static PreparedStatement getPrepared(String sql) {
        // Tenta obter um objeto PreparedStatement a partir de um SQL fornecido como parâmetro
        try {
            Connection con = getConnection();
            if (con != null) {
                return con.prepareStatement(sql);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao preparar o SQL: " + e.getMessage());
            return null;
        }
    }

    public static ResultSet getSelect(String sql) {
        // Tenta obter um objeto ResultSet a partir de um SQL fornecido como parâmetro
        try {
            PreparedStatement ps = getPrepared(sql);
            if (ps != null) {
                return ps.executeQuery();
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar a consulta: " + e.getMessage());
            return null;
        }
    }

    public static void close(PreparedStatement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar o Statement: " + e.getMessage());
        }
    }

    public static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar o ResultSet: " + e.getMessage());
        }
    }

    public static void close(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar a conexão: " + e.getMessage());
        }
    }
}
