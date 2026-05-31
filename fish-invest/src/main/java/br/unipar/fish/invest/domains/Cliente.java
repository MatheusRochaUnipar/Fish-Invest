package br.unipar.fish.invest.domains;

import java.time.LocalDate;

public class Cliente extends BaseEntity {
    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private String telefone;
    private LocalDate dataCadastro;

    private SegurancaAcesso segurancaAcesso;

    public Cliente() {
    }

    public Cliente(String nome, String cpf, String email, String senha, String telefone, LocalDate dataCadastro, SegurancaAcesso segurancaAcesso, Integer id) {
        super(id);
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.dataCadastro = dataCadastro;
        this.segurancaAcesso = segurancaAcesso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public SegurancaAcesso getSegurancaAcesso() {
        return segurancaAcesso;
    }

    public void setSegurancaAcesso(SegurancaAcesso segurancaAcesso) {
        this.segurancaAcesso = segurancaAcesso;
    }

    @Override
    public String toString() {
        return "Cliente{" + "nome=" + nome + ", cpf=" + cpf + ", email=" + email + ", senha=" + senha + ", telefone=" + telefone + ", dataCadastro=" + dataCadastro + ", segurancaAcesso=" + segurancaAcesso + '}';
    }   
}
