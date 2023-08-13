/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastrobd;

import cadastrobd.model.PessoaFisica;
import cadastrobd.model.PessoaFisicaDAO;
import cadastrobd.model.PessoaJuridica;
import cadastrobd.model.PessoaJuridicaDAO;

/**
 *
 * @author Mari
 */
public class CadastroBD {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // a. Instanciar uma pessoa física e persistir no banco de dados.
        PessoaFisica pessoaFisica = new PessoaFisica(1, "Maria", "Rua 1", "São Paulo", "SP", "11 11111111", "maria@gmail.com", "11111111111");
        PessoaFisicaDAO pessoaFisicaDAO = new PessoaFisicaDAO();
        pessoaFisicaDAO.incluir(pessoaFisica);

        //b. Alterar os dados da pessoa física no banco.
        pessoaFisica.setNome("Maria da Silva");
        pessoaFisicaDAO.alterar(pessoaFisica);

        // c. Consultar todas as pessoas físicas do banco de dados e listar no console.
        pessoaFisicaDAO.getPessoas().forEach((pessoa) -> {
            pessoa.exibir();
        });

        // d. Excluir a pessoa física criada anteriormente no banco.
        pessoaFisicaDAO.excluir(pessoaFisica.getId());

        // e. Instanciar uma pessoa jurídica e persistir no banco de dados.
        PessoaJuridica pessoaJuridica = new PessoaJuridica(2, "Empresa", "Rua 2", "São Paulo", "SP", "11 22222222", "empresa@gmail.com", "22222222222222");
        PessoaJuridicaDAO pessoaJuridicaDAO = new PessoaJuridicaDAO();
        pessoaJuridicaDAO.incluir(pessoaJuridica);

        // f. Alterar os dados da pessoa jurídica no banco.
        pessoaJuridica.setNome("Empresa LTDA");
        pessoaJuridicaDAO.alterar(pessoaJuridica);

        // g. Consultar todas as pessoas jurídicas do banco e listar no console.
        pessoaJuridicaDAO.getPessoas().forEach((pessoa) -> {
            pessoa.exibir();
        });

        // h. Excluir a pessoa jurídica criada anteriormente no banco.
        pessoaJuridicaDAO.excluir(pessoaJuridica.getId());
    }
}
