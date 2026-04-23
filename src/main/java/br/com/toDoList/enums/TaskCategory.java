package br.com.toDoList.enums;

// Enum para categoria das tarefas
public enum TaskCategory {
    ESTUDOS("ESTUDOS"),
    PESSOAL("PESSOAL"),
    TRABALHO("TRABALHO");

    private String description;

    TaskCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
