package br.unipar.fish.invest.exceptions;

public class RepositorioException extends Exception {

    public RepositorioException(String mensagem) {
        super(mensagem);
    }

    public RepositorioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}