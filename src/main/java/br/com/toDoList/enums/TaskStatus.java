package br.com.toDoList.enums;

// Enum para status das tarefas
public enum TaskStatus {
    TODO("A fazer"),
    DOING("EM andamento"),
    DONE("Concluído");

    private final String displayValue;

    TaskStatus(String displayValue){
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
