package br.unipar.fish.invest.domains;

public abstract class BaseEntity {

    private Integer id;

    public BaseEntity() {}

    public BaseEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BaseEntity{id=" + id + "}";
    }
}
