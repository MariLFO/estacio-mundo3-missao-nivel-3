package cadastrobd.model;

/**
 *
 * @author Mari
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import cadastrobd.model.util.ConectorBD;
import cadastrobd.model.util.SequenceManager;

public class PessoaJuridicaDAO {
    public PessoaJuridica getPessoa(int id) {
        try {
            Connection conexao = ConectorBD.getConnection();

            // Verifica se a conexão é válida
            if (conexao == null) {
                // Se a conexão for nula, retorna null
                return null;
            }

            // Cria um SQL para consultar os dados da pessoa jurídica pelo id
            String sql = "SELECT * FROM Pessoa p INNER JOIN PessoaJuridica pj ON p.idPessoa = pj.idPessoa WHERE p.idPessoa = ?";

            // Obtém um objeto PreparedStatement com o SQL criado e o id fornecido como parâmetro
            PreparedStatement prepared = ConectorBD.getPrepared(conexao, sql);
            prepared.setInt(1, id);

            // Executa a consulta e obtém um objeto ResultSet com o resultado
            ResultSet resultSet = ConectorBD.getSelect(prepared);

            // Verifica se o ResultSet é válido e contém algum dado
            if (resultSet != null && resultSet.next()) {
                // Cria um objeto PessoaJuridica com os dados obtidos do ResultSet
                PessoaJuridica pessoaJuridica = criaPessoaJuridica(resultSet);

                // Fecha os objetos ResultSet, PreparedStatement e Connection
                ConectorBD.close(resultSet);
                ConectorBD.close(prepared);
                ConectorBD.close(conexao);
                return pessoaJuridica;
            }

            // Se o ResultSet for nulo ou vazio, fecha os objetos PreparedStatement e Connection e retorna null
            ConectorBD.close(prepared);
            ConectorBD.close(conexao);
            return null;
        } catch (SQLException e) {
            // Se ocorrer algum erro, imprime a mensagem no console e retorna null
            System.out.println("Erro ao obter a pessoa jurídica pelo id: " + e.getMessage());
            return null;
        }
    }

    public List<PessoaJuridica> getPessoas() {
        try {
            // Obtém uma conexão com o banco de dados
            Connection conexao = ConectorBD.getConnection();

            // Verifica se a conexão é válida
            if (conexao == null) {
                // Se a conexão for nula, retorna null
                return null;
            }

            // Cria um SQL para consultar todos os dados das pessoas jurídicas
            String sql = "SELECT * FROM Pessoa p INNER JOIN PessoaJuridica pf ON p.idPessoa = pf.idPessoa";

            // Obtém um objeto PreparedStatement com o SQL criado
            PreparedStatement prepared = conexao.prepareStatement(sql);

            // Executa a consulta e obtém um objeto ResultSet com o resultado
            ResultSet resultSet = ConectorBD.getSelect(prepared);

            // Cria uma lista de objetos PessoaJuridica para armazenar os dados obtidos
            List<PessoaJuridica> pessoas = new ArrayList<>();

            // Percorre o ResultSet enquanto houver dados
            while (resultSet != null && resultSet.next()) {
                // Cria um objeto PessoaJuridica com os dados obtidos do ResultSet
                PessoaJuridica pessoaJuridica = criaPessoaJuridica(resultSet);
                pessoas.add(pessoaJuridica);
            }

            // Fecha os objetos ResultSet, PreparedStatement e Connection
            ConectorBD.close(resultSet);
            ConectorBD.close(prepared);
            ConectorBD.close(conexao);

            // Retorna a lista de objetos PessoaJuridica criada
            return pessoas;
        } catch (SQLException e) {
            // Se ocorrer algum erro, imprime a mensagem no console e retorna null
            System.out.println("Erro ao obter todas as pessoas jurídicas: " + e.getMessage());
            return null;
        }
    }

    public boolean incluir(PessoaJuridica pessoaJuridica) {
        try {
            Integer nextId = SequenceManager.getValue("PessoaSequence");

            if (nextId == -1) {
                // Se não foi possível obter o próximo id da sequência, retorna false
                return false;
            }

            pessoaJuridica.setId(nextId);
            Connection conexao = ConectorBD.getConnection();

            // Verifica se a conexão é válida
            if (conexao == null) {
                // Se a conexão for nula, retorna false
                return false;
            }

            // Cria um SQL para inserir os dados da pessoa na tabela Pessoa
            String sql = "INSERT INTO Pessoa (idPessoa, nome, telefone, email, logradouro, cidade, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

            // Obtém um objeto PreparedStatement com o SQL criado e os dados da pessoa jurídica fornecida como parâmetro
            PreparedStatement prepared = ConectorBD.getPrepared(conexao, sql);
            prepared.setInt(1, pessoaJuridica.getId());
            prepared.setString(2, pessoaJuridica.getNome());
            prepared.setString(3, pessoaJuridica.getTelefone());
            prepared.setString(4, pessoaJuridica.getEmail());
            prepared.setString(5, pessoaJuridica.getLogradouro());
            prepared.setString(6, pessoaJuridica.getCidade());
            prepared.setString(7, pessoaJuridica.getEstado());

            // Executa a inserção e verifica se foi bem sucedida
            if (prepared.executeUpdate() <= 0) {
                // Se a inserção na tabela Pessoa falhou, fecha os objetos PreparedStatement e Connection e retorna false
                ConectorBD.close(prepared);
                ConectorBD.close(conexao);
                return false;
            }

            // Se a inserção na tabela Pessoa foi bem sucedida, cria um SQL para inserir os dados da pessoa jurídica na tabela PessoaJuridica
            sql = "INSERT INTO PessoaJuridica (idPessoa, cnpj) VALUES (?, ?)";

            // Obtém um objeto PreparedStatement com o SQL criado e os dados da pessoa jurídica fornecida como parâmetro
            prepared = ConectorBD.getPrepared(conexao, sql);
            prepared.setInt(1, nextId);
            prepared.setString(2, pessoaJuridica.getCnpj());

            // Executa a inserção e verifica se foi bem sucedida
            if (prepared.executeUpdate() <= 0) {
                // Se a inserção na tabela PessoaJuridica falhou, fecha os objetos PreparedStatement e Connection e retorna false
                ConectorBD.close(prepared);
                ConectorBD.close(conexao);
                return false;
            }

            // Se a inserção na tabela PessoaJuridica foi bem sucedida, fecha os objetos PreparedStatement e Connection e retorna true
            ConectorBD.close(prepared);
            ConectorBD.close(conexao);
            return true;
        } catch (SQLException e) {
            // Se ocorrer algum erro, imprime a mensagem no console e retorna false
            System.out.println("Erro ao incluir a pessoa jurídica: " + e.getMessage());
            return false;
        }
    }

    public boolean alterar(PessoaJuridica pessoaJuridica) {
        try {
            Connection conexao = ConectorBD.getConnection();

            // Verifica se a conexão é válida
            if (conexao == null) {
                // Se a conexão for nula, retorna false
                return false;
            }

            // Cria um SQL para atualizar os dados da pessoa na tabela Pessoa
            String sql = "UPDATE Pessoa SET nome = ?, telefone = ?, email = ?, logradouro = ?, cidade = ?, estado = ? WHERE idPessoa = ?";

            // Obtém um objeto PreparedStatement com o SQL criado e os dados da pessoa jurídica fornecida como parâmetro
            PreparedStatement prepared = ConectorBD.getPrepared(conexao, sql);
            prepared.setString(1, pessoaJuridica.getNome());
            prepared.setString(2, pessoaJuridica.getTelefone());
            prepared.setString(3, pessoaJuridica.getEmail());
            prepared.setString(4, pessoaJuridica.getLogradouro());
            prepared.setString(5, pessoaJuridica.getCidade());
            prepared.setString(6, pessoaJuridica.getEstado());
            prepared.setInt(7, pessoaJuridica.getId());

            // Executa a atualização e verifica se foi bem sucedida
            if (prepared.executeUpdate() <= 0) {
                // Se a atualização na tabela Pessoa falhou, fecha os objetos PreparedStatement e Connection e retorna false
                ConectorBD.close(prepared);
                ConectorBD.close(conexao);
                return false;
            }

            // Se a atualização na tabela Pessoa foi bem sucedida, cria um SQL para atualizar os dados da pessoa jurídica na tabela PessoaJuridica
            sql = "UPDATE PessoaJuridica SET cnpj = ? WHERE idPessoa = ?";

            // Obtém um objeto PreparedStatement com o SQL criado e os dados da pessoa jurídica fornecida como parâmetro
            prepared = ConectorBD.getPrepared(conexao, sql);
            prepared.setString(1, pessoaJuridica.getCnpj());
            prepared.setInt(2, pessoaJuridica.getId());

            // Executa a atualização e verifica se foi bem sucedida
            if (prepared.executeUpdate() <= 0) {
                // Se a atualização na tabela PessoaJuridica falhou, fecha os objetos PreparedStatement e Connection e retorna false
                ConectorBD.close(prepared);
                ConectorBD.close(conexao);
                return false;
            }

            // Se a atualização na tabela PessoaJuridica foi bem sucedida, fecha os objetos PreparedStatement e Connection e retorna true
            ConectorBD.close(prepared);
            ConectorBD.close(conexao);
            return true;
        } catch (SQLException e) {
            // Se ocorrer algum erro, imprime a mensagem no console e retorna false
            System.out.println("Erro ao alterar a pessoa jurídica: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        try {
            Connection conexao = ConectorBD.getConnection();

            // Verifica se a conexão é válida
            if (conexao == null) {
                // Se a conexão for nula, retorna false
                return false;
            }

            // Cria um SQL para excluir os dados da pessoa jurídica na tabela PessoaJuridica
            String sql = "DELETE FROM PessoaJuridica WHERE idPessoa = ?";

            // Obtém um objeto PreparedStatement com o SQL criado e o id fornecido como parâmetro
            PreparedStatement prepared = ConectorBD.getPrepared(conexao, sql);
            prepared.setInt(1, id);

            // Executa a exclusão e verifica se foi bem sucedida
            if (prepared.executeUpdate() <= 0) {
                // Se a exclusão na tabela PessoaJuridica falhou, fecha os objetos PreparedStatement e Connection e retorna false
                ConectorBD.close(prepared);
                ConectorBD.close(conexao);
                return false;
            }

            // Se a exclusão na tabela PessoaJuridica foi bem sucedida, cria um SQL para excluir os dados da pessoa na tabela Pessoa
            sql = "DELETE FROM Pessoa WHERE idPessoa = ?";

            // Obtém um objeto PreparedStatement com o SQL criado e o id fornecido como parâmetro
            prepared = ConectorBD.getPrepared(conexao, sql);
            prepared.setInt(1, id); // Substitua 1 pelo índice do campo id na tabela Pessoa

            // Executa a exclusão e verifica se foi bem sucedida
            if (prepared.executeUpdate() <= 0) {
                // Se a exclusão na tabela Pessoa falhou, fecha os objetos PreparedStatement e Connection e retorna false
                ConectorBD.close(prepared);
                ConectorBD.close(conexao);
                return false;
            }

            // Se a exclusão na tabela Pessoa foi bem sucedida, fecha os objetos PreparedStatement e Connection e retorna true
            ConectorBD.close(prepared);
            ConectorBD.close(conexao);
            return true;
        } catch (SQLException e) {
            // Se ocorrer algum erro, imprime a mensagem no console e retorna false
            System.out.println("Erro ao excluir a pessoa jurídica: " + e.getMessage());
            return false;
        }
    }

    private static PessoaJuridica criaPessoaJuridica(ResultSet resultSet) throws SQLException {
        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        pessoaJuridica.setId(resultSet.getInt("idPessoa"));
        pessoaJuridica.setNome(resultSet.getString("nome"));
        pessoaJuridica.setTelefone(resultSet.getString("telefone"));
        pessoaJuridica.setEmail(resultSet.getString("email"));
        pessoaJuridica.setLogradouro(resultSet.getString("logradouro"));
        pessoaJuridica.setCidade(resultSet.getString("cidade"));
        pessoaJuridica.setEstado(resultSet.getString("estado"));
        pessoaJuridica.setCnpj(resultSet.getString("cnpj"));
        return pessoaJuridica;
    }
}
