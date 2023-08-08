package cadastrobd.model;

/**
 *
 * @author Mari
 */

public class PessoaFisica extends Pessoa {
    private String cpf;

    public PessoaFisica() {
        super();
        this.cpf = "";
    }

    public PessoaFisica(int id, String nome, String logradouro, String cidade, String estado, String telefone, String email, String cpf) {
        super(id, nome, logradouro, cidade, estado, telefone, email);
        this.cpf = cpf;
    }

    @Override
    public void exibir() {
        super.exibir();
        System.out.println("CPF: " + this.cpf);
    }
}