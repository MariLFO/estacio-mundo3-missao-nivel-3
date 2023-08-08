package cadastrobd.model.util;

/**
 *
 * @author Mari
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SequenceManager {
    private static final String DATABASE = ""; // Nome do banco de dados
    private static final String TABLE = ""; // Nome da tabela que contém as sequências

    public static int getValue(String sequence) {
        try {
            Connection conexao = ConectorBD.getConnection();
            if (conexao != null) {
                // Cria um SQL para consultar o próximo valor da sequência
                String sql = "SELECT NEXT VALUE FOR " + DATABASE + "." + TABLE + "." + sequence;
                PreparedStatement consulta = ConectorBD.getPrepared(conexao, sql);
                ResultSet resultado = ConectorBD.getSelect(consulta);

                // Verifica se o ResultSet é válido e contém algum dado
                if (resultado != null && resultado.next()) {
                    // Obtém o próximo valor da sequência como um inteiro
                    int value = resultado.getInt(1);
                    // Fecha os objetos ResultSet, PreparedStatement e Connection
                    ConectorBD.close(resultado);
                    ConectorBD.close(consulta);
                    ConectorBD.close(conexao);
                    // Retorna o valor obtido
                    return value;
                } else {
                    ConectorBD.close(conexao);
                    return -1;
                }
            } else {
                // Se a conexão for nula, retorna -1
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter o valor da sequência: " + e.getMessage());
            return -1;
        }
    }
}
