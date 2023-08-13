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

public class PessoaFisicaDAO {
    public PessoaFisica getPessoa(int id) {
        try {
            Connection conexao = ConectorBD.getConnection();

            // Verifica se a conexão é válida
            if (conexao == null) {
                // Se a conexão for nula, retorna null
                return null;
            }

            // Cria um SQL para consultar os dados da pessoa física pelo id
            String sql = "SELECT * FROM Pessoa p INNER JOIN PessoaFisica pf ON p.idPessoa = pf.idPessoa WHERE p.idPessoa = ?";

            // Obtém um objeto PreparedStatement com o SQL criado e o id fornecido como parâmetro
            PreparedStatement prepared = ConectorBD.getPrepared(conexao, sql);
            prepared.setInt(1, id);

            // Executa a consulta e obtém um objeto ResultSet com o resultado
            ResultSet resultSet = ConectorBD.getSelect(prepared);

            // Verifica se o ResultSet é válido e contém algum dado
            if (resultSet != null && resultSet.next()) {
                // Cria um objeto PessoaFisica com os dados obtidos do ResultSet
                PessoaFisica pessoaFisica = criaPessoaFisica(resultSet);

                // Fecha os objetos ResultSet, PreparedStatement e Connection
                ConectorBD.close(resultSet);
                ConectorBD.close(prepared);
                ConectorBD.close(conexao);
                return pessoaFisica;
            }

            // Se o ResultSet for nulo ou vazio, fecha os objetos PreparedStatement e Connection e retorna null
            ConectorBD.close(prepared);
            ConectorBD.close(conexao);
            return null;
        } catch (SQLException e) {
            // Se ocorrer algum erro, imprime a mensagem no console e retorna null
            System.out.println("Erro ao obter a pessoa física pelo id: " + e.getMessage());
            return null;
        }
    }

    public List<PessoaFisica> getPessoas() {
        try {
            // Obtém uma conexão com o banco de dados
            Connection conexao = ConectorBD.getConnection();

            // Verifica se a conexão é válida
            if (conexao == null) {
                // Se a conexão for nula, retorna null
                return null;
            }

            // Cria um SQL para consultar todos os dados das pessoas físicas
            String sql = "SELECT * FROM Pessoa p INNER JOIN PessoaFisica pf ON p.idPessoa = pf.idPessoa";

            // Obtém um objeto PreparedStatement com o SQL criado
            PreparedStatement prepared = conexao.prepareStatement(sql);

            // Executa a consulta e obtém um objeto ResultSet com o resultado
            ResultSet resultSet = ConectorBD.getSelect(prepared);

            // Cria uma lista de objetos PessoaFisica para armazenar os dados obtidos
            List<PessoaFisica> pessoas = new ArrayList<>();

            // Percorre o ResultSet enquanto houver dados
            while (resultSet != null && resultSet.next()) {
                // Cria um objeto PessoaFisica com os dados obtidos do ResultSet
                PessoaFisica pessoaFisica = criaPessoaFisica(resultSet);
                pessoas.add(pessoaFisica);
            }

            // Fecha os objetos ResultSet, PreparedStatement e Connection
            ConectorBD.close(resultSet);
            ConectorBD.close(prepared);
            ConectorBD.close(conexao);

            // Retorna a lista de objetos PessoaFisica criada
            return pessoas;
        } catch (SQLException e) {
            // Se ocorrer algum erro, imprime a mensagem no console e retorna null
            System.out.println("Erro ao obter todas as pessoas físicas: " + e.getMessage());
            return null;
        }
    }

    public boolean incluir(PessoaFisica pessoaFisica) {
        try {
            Integer nextId = SequenceManager.getValue("PessoaSequence");

            if (nextId == -1) {
                // Se não foi possível obter o próximo id da sequência, retorna false
                return false;
            }

            pessoaFisica.setId(nextId);
            Connection conexao = ConectorBD.getConnection();

            // Verifica se a conexão é válida
            if (conexao == null) {
                // Se a conexão for nula, retorna false
                return false;
            }

            // Cria um SQL para inserir os dados da pessoa na tabela Pessoa
            String sql = "INSERT INTO Pessoa (idPessoa, nome, telefone, email, logradouro, cidade, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

            // Obtém um objeto PreparedStatement com o SQL criado e os dados da pessoa física fornecida como parâmetro
            PreparedStatement prepared = ConectorBD.getPrepared(conexao, sql);
            prepared.setInt(1, pessoaFisica.getId());
            prepared.setString(2, pessoaFisica.getNome());
            prepared.setString(3, pessoaFisica.getTelefone());
            prepared.setString(4, pessoaFisica.getEmail());
            prepared.setString(5, pessoaFisica.getLogradouro());
            prepared.setString(6, pessoaFisica.getCidade());
            prepared.setString(7, pessoaFisica.getEstado());

            // Executa a inserção e verifica se foi bem sucedida
            if (prepared.executeUpdate() <= 0) {
                // Se a inserção na tabela Pessoa falhou, fecha os objetos PreparedStatement e Connection e retorna false
                ConectorBD.close(prepared);
                ConectorBD.close(conexao);
                return false;
            }

            // Se a inserção na tabela Pessoa foi bem sucedida, cria um SQL para inserir os dados da pessoa física na tabela PessoaFisica
            sql = "INSERT INTO PessoaFisica (idPessoa, cpf) VALUES (?, ?)";

            // Obtém um objeto PreparedStatement com o SQL criado e os dados da pessoa física fornecida como parâmetro
            prepared = ConectorBD.getPrepared(conexao, sql);
            prepared.setInt(1, nextId);
            prepared.setString(2, pessoaFisica.getCpf());

            // Executa a inserção e verifica se foi bem sucedida
            if (prepared.executeUpdate() <= 0) {
                // Se a inserção na tabela PessoaFisica falhou, fecha os objetos PreparedStatement e Connection e retorna false
                ConectorBD.close(prepared);
                ConectorBD.close(conexao);
                return false;
            }

            // Se a inserção na tabela PessoaFisica foi bem sucedida, fecha os objetos PreparedStatement e Connection e retorna true
            ConectorBD.close(prepared);
            ConectorBD.close(conexao);
            return true;
        } catch (SQLException e) {
            // Se ocorrer algum erro, imprime a mensagem no console e retorna false
            System.out.println("Erro ao incluir a pessoa física: " + e.getMessage());
            return false;
        }
    }

    public boolean alterar(PessoaFisica pessoaFisica) {
        try {
            Connection conexao = ConectorBD.getConnection();

            // Verifica se a conexão é válida
            if (conexao == null) {
                // Se a conexão for nula, retorna false
                return false;
            }

            // Cria um SQL para atualizar os dados da pessoa na tabela Pessoa
            String sql = "UPDATE Pessoa SET nome = ?, telefone = ?, email = ?, logradouro = ?, cidade = ?, estado = ? WHERE idPessoa = ?";

            // Obtém um objeto PreparedStatement com o SQL criado e os dados da pessoa física fornecida como parâmetro
            PreparedStatement prepared = ConectorBD.getPrepared(conexao, sql);
            prepared.setString(1, pessoaFisica.getNome());
            prepared.setString(2, pessoaFisica.getTelefone());
            prepared.setString(3, pessoaFisica.getEmail());
            prepared.setString(4, pessoaFisica.getLogradouro());
            prepared.setString(5, pessoaFisica.getCidade());
            prepared.setString(6, pessoaFisica.getEstado());
            prepared.setInt(7, pessoaFisica.getId());

            // Executa a atualização e verifica se foi bem sucedida
            if (prepared.executeUpdate() <= 0) {
                // Se a atualização na tabela Pessoa falhou, fecha os objetos PreparedStatement e Connection e retorna false
                ConectorBD.close(prepared);
                ConectorBD.close(conexao);
                return false;
            }

            // Se a atualização na tabela Pessoa foi bem sucedida, cria um SQL para atualizar os dados da pessoa física na tabela PessoaFisica
            sql = "UPDATE PessoaFisica SET cpf = ? WHERE idPessoa = ?";

            // Obtém um objeto PreparedStatement com o SQL criado e os dados da pessoa física fornecida como parâmetro
            prepared = ConectorBD.getPrepared(conexao, sql);
            prepared.setString(1, pessoaFisica.getCpf());
            prepared.setInt(2, pessoaFisica.getId());

            // Executa a atualização e verifica se foi bem sucedida
            if (prepared.executeUpdate() <= 0) {
                // Se a atualização na tabela PessoaFisica falhou, fecha os objetos PreparedStatement e Connection e retorna false
                ConectorBD.close(prepared);
                ConectorBD.close(conexao);
                return false;
            }

            // Se a atualização na tabela PessoaFisica foi bem sucedida, fecha os objetos PreparedStatement e Connection e retorna true
            ConectorBD.close(prepared);
            ConectorBD.close(conexao);
            return true;
        } catch (SQLException e) {
            // Se ocorrer algum erro, imprime a mensagem no console e retorna false
            System.out.println("Erro ao alterar a pessoa física: " + e.getMessage());
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

            // Cria um SQL para excluir os dados da pessoa física na tabela PessoaFisica
            String sql = "DELETE FROM PessoaFisica WHERE idPessoa = ?";

            // Obtém um objeto PreparedStatement com o SQL criado e o id fornecido como parâmetro
            PreparedStatement prepared = ConectorBD.getPrepared(conexao, sql);
            prepared.setInt(1, id);

            // Executa a exclusão e verifica se foi bem sucedida
            if (prepared.executeUpdate() <= 0) {
                // Se a exclusão na tabela PessoaFisica falhou, fecha os objetos PreparedStatement e Connection e retorna false
                ConectorBD.close(prepared);
                ConectorBD.close(conexao);
                return false;
            }

            // Se a exclusão na tabela PessoaFisica foi bem sucedida, cria um SQL para excluir os dados da pessoa na tabela Pessoa
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
            System.out.println("Erro ao excluir a pessoa física: " + e.getMessage());
            return false;
        }
    }

    private static PessoaFisica criaPessoaFisica(ResultSet resultSet) throws SQLException {
        PessoaFisica pessoaFisica = new PessoaFisica();
        pessoaFisica.setId(resultSet.getInt("idPessoa"));
        pessoaFisica.setNome(resultSet.getString("nome"));
        pessoaFisica.setTelefone(resultSet.getString("telefone"));
        pessoaFisica.setEmail(resultSet.getString("email"));
        pessoaFisica.setLogradouro(resultSet.getString("logradouro"));
        pessoaFisica.setCidade(resultSet.getString("cidade"));
        pessoaFisica.setEstado(resultSet.getString("estado"));
        pessoaFisica.setCpf(resultSet.getString("cpf"));
        return pessoaFisica;
    }
}
