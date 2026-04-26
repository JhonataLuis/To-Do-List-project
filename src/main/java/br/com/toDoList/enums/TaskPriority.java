package br.com.toDoList.enums;

public enum TaskPriority {
    BAIXA("Baixa"),
    MEDIA("Media"),
    ALTA("Alta"),
    URGENTE("Urgente");

    private String descriptionPriority;

    TaskPriority(String descriptionPriotiry){
        this.descriptionPriority = descriptionPriotiry;
    }

    public String getDescriptionPriority() {
        return descriptionPriority;
    }

    // Método para valores em letras minúsculas e maiúsculas
    public static TaskPriority fromString(String value) {
        return TaskPriority.valueOf(value.trim().toUpperCase());
    }

}
