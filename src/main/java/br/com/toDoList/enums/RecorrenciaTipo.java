package br.com.toDoList.enums;

public enum RecorrenciaTipo {

    NENHUMA("Nenhuma"),
    DIARIA("Diária"),
    SEMANAL("Semanal"),
    MENSAL("Mensal");

    private String descricao;

    RecorrenciaTipo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
