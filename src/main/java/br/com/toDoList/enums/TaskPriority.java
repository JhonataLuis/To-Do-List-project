package br.com.toDoList.enums;

public enum TaskPriority {
    BAIXA("BAIXA"),
    MEDIA("MEDIA"),
    ALTA("ALTA"),
    URGENTE("URGENTE");

    private String descriptionPriority;

    TaskPriority(String descriptionPriotiry){
        this.descriptionPriority = descriptionPriotiry;
    }

    public String getDescriptionPriority() {
        return descriptionPriority;
    }

}
