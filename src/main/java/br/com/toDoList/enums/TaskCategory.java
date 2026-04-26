package br.com.toDoList.enums;

// Enum para categoria das tarefas
public enum TaskCategory {
    ESTUDOS("Estudos"),
    PESSOAL("Pessoal"),
    TRABALHO("Trabalho");

    private String description;

    TaskCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // Método para valores em letras minúsculas e maiúsculas
    public static TaskCategory fromString(String value) {
        return TaskCategory.valueOf(value.trim().toUpperCase());
    }
}

