package br.unipar.fish.invest.domains;

public class TipoInvestimento extends BaseEntity {

    private String nomeTipo;

    public TipoInvestimento() {
    }

    public TipoInvestimento(Integer id, String nomeTipo) {
        super(id);
        this.nomeTipo = nomeTipo;
    }

    public String getNomeTipo() {
        return nomeTipo;
    }

    public void setNomeTipo(String nomeTipo) {
        this.nomeTipo = nomeTipo;
    }

    @Override
    public String toString() {
        return "TipoInvestimento{" + "nomeTipo=" + nomeTipo + '}';
    }    
}