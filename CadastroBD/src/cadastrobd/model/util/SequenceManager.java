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

    public static int getValue(String sequence) {
        try {
            Connection conexao = ConectorBD.getConnection();

            // Verifica se a conexão é válida
            if (conexao == null) {
                // Se a conexão for nula, retorna -1
                return -1;
            }

            // Cria um SQL para consultar o próximo valor da sequência
            String sql = "SELECT NEXT VALUE FOR dbo." + sequence;
            PreparedStatement consulta = ConectorBD.getPrepared(conexao, sql);
            ResultSet resultado = ConectorBD.getSelect(consulta);

            // Verifica se o ResultSet é válido e contém algum dado
            if (resultado == null || !resultado.next()) {
                // Se o ResultSet for nulo ou não contiver dados, retorna -1
                ConectorBD.close(conexao);
                return -1;
            }

            // Obtém o próximo valor da sequência como um inteiro
            int value = resultado.getInt(1);

            // Fecha os objetos ResultSet, PreparedStatement e Connection
            ConectorBD.close(resultado);
            ConectorBD.close(consulta);
            ConectorBD.close(conexao);

            return value;
        } catch (SQLException e) {
            System.out.println("Erro ao obter o valor da sequência: " + e.getMessage());
            return -1;
        }
    }
}
