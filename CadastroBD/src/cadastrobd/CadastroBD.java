package cadastrobd;

import cadastrobd.model.PessoaFisica;
import cadastrobd.model.PessoaFisicaDAO;
import cadastrobd.model.PessoaJuridica;
import cadastrobd.model.PessoaJuridicaDAO;

import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Mari
 */
public class CadastroBD {
    private static Scanner scanner = new Scanner(System.in);
    private static PessoaFisicaDAO pessoaFisicaDAO = new PessoaFisicaDAO();
    private static PessoaJuridicaDAO pessoaJurdicaDAO = new PessoaJuridicaDAO();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("===========================================");
            System.out.println("Selecione uma opção:");
            System.out.println("1 - Incluir Pessoa");
            System.out.println("2 - Alterar Pessoa");
            System.out.println("3 - Excluir Pessoa");
            System.out.println("4 - Exibir pelo id");
            System.out.println("5 - Exibir todos");
            System.out.println("0 - Finalizar a execução");

            opcao = Integer.parseInt(scanner.nextLine());

            System.out.println("===========================================");
            switch (opcao) {
                case 1:
                    inserirPessoa();
                    break;
                case 2:
                    alterarPessoa();
                    break;
                case 3:
                    excluirPessoa();
                    break;
                case 4:
                    exibirPessoaPeloId();
                    break;
                case 5:
                    exibirTodasAsPessoas();
                    break;
            }

            System.out.println();
        }
    }

    private static String lerTipoDePessoa() {
        System.out.println("Escolha o tipo:\n\tPara Pessoa Física digite F\n\tPara Pessoa Jurídica digite J");
        String tipo = scanner.nextLine();

        System.out.println("===========================================\n");

        if (tipo.equalsIgnoreCase("F") || tipo.equalsIgnoreCase("J")) {
            return tipo;
        } else {
            System.out.println("Opção inválida, tente novamente.");
            return lerTipoDePessoa();
        }
    }

    private static PessoaFisica definirDadosPessoaFisica(PessoaFisica pessoaFisica) {
        try {
            System.out.println("Digite o nome: ");
            pessoaFisica.setNome(scanner.nextLine());
            System.out.println("Digite o cpf: ");
            pessoaFisica.setCpf(scanner.nextLine());
            System.out.println("Digite o telefone: ");
            pessoaFisica.setTelefone(scanner.nextLine());
            System.out.println("Digite o email: ");
            pessoaFisica.setEmail(scanner.nextLine());
            System.out.println("Digite o logradouro: ");
            pessoaFisica.setLogradouro(scanner.nextLine());
            System.out.println("Digite a cidade: ");
            pessoaFisica.setCidade(scanner.nextLine());
            System.out.println("Digite o estado: ");
            pessoaFisica.setEstado(scanner.nextLine());
            return pessoaFisica;
        } catch (Exception e) {
            System.out.println("Erro ao inserir os dados da Pessoa física:");
            e.printStackTrace();
            System.out.println("Por favor, tente novamente.");
            return null;
        }
    }

    private static PessoaJuridica definirDadosPessoaJuridica(PessoaJuridica pessoaJuridica) {
        try {
            System.out.println("Digite o nome: ");
            pessoaJuridica.setNome(scanner.nextLine());
            System.out.println("Digite o cpf: ");
            pessoaJuridica.setCnpj(scanner.nextLine());
            System.out.println("Digite o telefone: ");
            pessoaJuridica.setTelefone(scanner.nextLine());
            System.out.println("Digite o email: ");
            pessoaJuridica.setEmail(scanner.nextLine());
            System.out.println("Digite o logradouro: ");
            pessoaJuridica.setLogradouro(scanner.nextLine());
            System.out.println("Digite a cidade: ");
            pessoaJuridica.setCidade(scanner.nextLine());
            System.out.println("Digite o estado: ");
            pessoaJuridica.setEstado(scanner.nextLine());
            return pessoaJuridica;
        } catch (Exception e) {
            System.out.println("Erro ao inserir os dados da Pessoa Jurídica:");
            e.printStackTrace();
            System.out.println("Por favor, tente novamente.");
            return null;
        }
    }

    private static void inserirPessoa() {
        String tipo = lerTipoDePessoa();

        if (tipo.equalsIgnoreCase("F")) {
            PessoaFisica pessoaFisica = definirDadosPessoaFisica(new PessoaFisica());
            if (pessoaFisica == null) {
                System.out.println("Falha ao incluir Pessoa Física.");
                return;
            }
            if (pessoaFisicaDAO.incluir(pessoaFisica) == false) {
                System.out.println("Falha ao incluir Pessoa Física.");
                return;
            }
            System.out.println("Pessoa Física incluída com sucesso.");
            return;
        }
        if (tipo.equalsIgnoreCase("J")) {
            PessoaJuridica pessoaJuridica = definirDadosPessoaJuridica(new PessoaJuridica());
            if (pessoaJuridica == null) {
                System.out.println("Falha ao incluir Pessoa Jurídica.");
                return;
            }
            if (pessoaJurdicaDAO.incluir(pessoaJuridica) == false) {
                System.out.println("Falha ao incluir Pessoa Jurídica.");
                return;
            }
            System.out.println("Pessoa Jurídica incluída com sucesso.");
        }
    }

    private static void alterarPessoa() {
        String tipo = lerTipoDePessoa();

        if (tipo.equalsIgnoreCase("F")) {
            System.out.println("Digite o id da Pessoa Física que deseja alterar: ");
            int idPessoaFisica = Integer.parseInt(scanner.nextLine());
            PessoaFisica pessoaFisica = pessoaFisicaDAO.getPessoa(idPessoaFisica);

            if (pessoaFisica == null) {
                System.out.println("Pessoa Física não encontrada.");
                return;
            }
            pessoaFisica.exibir();
            pessoaFisica = definirDadosPessoaFisica(pessoaFisica);

            if (pessoaFisica == null) {
                System.out.println("Falha ao alterar Pessoa Física.");
                return;
            }

            if (pessoaFisicaDAO.alterar(pessoaFisica) == false) {
                System.out.println("Falha ao alterar Pessoa Física.");
                return;
            }
            System.out.println("Pessoa Física alterada com sucesso.");
            return;
        }

        if (tipo.equalsIgnoreCase("J")) {
            System.out.println("Digite o id da Pessoa Jurídica que deseja alterar: ");
            int idPessoaJuridica = Integer.parseInt(scanner.nextLine());
            PessoaJuridica pessoaJuridica = pessoaJurdicaDAO.getPessoa(idPessoaJuridica);

            if (pessoaJuridica == null) {
                System.out.println("Pessoa Jurídica não encontrada.");
                return;
            }

            pessoaJuridica.exibir();
            pessoaJuridica = definirDadosPessoaJuridica(pessoaJuridica);

            if (pessoaJuridica == null) {
                System.out.println("Falha ao alterar Pessoa Jurídica.");
                return;
            }

            if (pessoaJurdicaDAO.alterar(pessoaJuridica) == false) {
                System.out.println("Falha ao alterar Pessoa Jurídica.");
                return;
            }

            System.out.println("Pessoa Jurídica alterada com sucesso.");
        }
    }

    private static void excluirPessoa() {
        String tipo = lerTipoDePessoa();

        if (tipo.equalsIgnoreCase("F")) {
            System.out.println("Digite o id da Pessoa Física que deseja excluir: ");
            int idPessoaFisica = Integer.parseInt(scanner.nextLine());
            PessoaFisica pessoaFisica = pessoaFisicaDAO.getPessoa(idPessoaFisica);

            if (pessoaFisica == null) {
                System.out.println("Pessoa Física não encontrada.");
                return;
            }

            if (pessoaFisicaDAO.excluir(idPessoaFisica) == false) {
                System.out.println("Falha ao excluir Pessoa Física.");
                return;
            }

            System.out.println("Pessoa Física excluída com sucesso.");
            return;
        }

        if (tipo.equalsIgnoreCase("J")) {
            System.out.println("Digite o id da Pessoa Jurídica que deseja excluir: ");
            int idPessoaJuridica = Integer.parseInt(scanner.nextLine());
            PessoaJuridica pessoaJuridica = pessoaJurdicaDAO.getPessoa(idPessoaJuridica);

            if (pessoaJuridica == null) {
                System.out.println("Pessoa Jurídica não encontrada.");
                return;
            }

            if (pessoaJurdicaDAO.excluir(idPessoaJuridica) == false) {
                System.out.println("Falha ao excluir Pessoa Jurídica.");
                return;
            }

            System.out.println("Pessoa Jurídica excluída com sucesso.");
        }
    }

    private static void exibirPessoaPeloId() {
        String tipo = lerTipoDePessoa();

        if (tipo.equalsIgnoreCase("F")) {
            System.out.println("Digite o id da Pessoa Física que deseja exibir: ");
            int idPessoaFisica = Integer.parseInt(scanner.nextLine());
            PessoaFisica pessoaFisica = pessoaFisicaDAO.getPessoa(idPessoaFisica);

            if (pessoaFisica == null) {
                System.out.println("Pessoa Física não encontrada.");
                return;
            }
            pessoaFisica.exibir();
            return;
        }

        if (tipo.equalsIgnoreCase("J")) {
            System.out.println("Digite o id da Pessoa Jurídica que deseja exibir: ");
            int idPessoaJuridica = Integer.parseInt(scanner.nextLine());
            PessoaJuridica pessoaJuridica = pessoaJurdicaDAO.getPessoa(idPessoaJuridica);

            if (pessoaJuridica == null) {
                System.out.println("Pessoa Jurídica não encontrada.");
                return;
            }
            pessoaJuridica.exibir();
        }
    }

    private static void exibirTodasAsPessoas() {
        String tipo = lerTipoDePessoa();

        if (tipo.equalsIgnoreCase("F")) {
            List<PessoaFisica> pessoasFisicas = pessoaFisicaDAO.getPessoas();
            if (pessoasFisicas == null) {
                System.out.println("Não existem Pessoas Físicas cadastradas.");
                return;
            }

            System.out.println("Exibindo todos os registros de Pessoas Físicas:\n");
            for (PessoaFisica pessoaFisica : pessoasFisicas) {
                System.out.println("-------------------------------------------");
                pessoaFisica.exibir();
            }
            return;
        }

        if (tipo.equalsIgnoreCase("J")) {
            List<PessoaJuridica> pessoasJuridicas = pessoaJurdicaDAO.getPessoas();
            if (pessoasJuridicas == null) {
                System.out.println("Não existem Pessoas Jurídicas cadastradas.");
                return;
            }

            System.out.println("Exibindo todos os registros de Pessoas Jurídicas.");
            for (PessoaJuridica pessoaJuridica : pessoasJuridicas) {
                System.out.println("-------------------------------------------");
                pessoaJuridica.exibir();
            }
        }
    }
}
