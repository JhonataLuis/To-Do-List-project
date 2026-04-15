package br.com.toDoList.dto;

// Dto record para reordenar lista conforme o usuário mobile quer mover
public record ReorderRequest(
    Long tarefaId,
    Double posAnterior,
    Double posProxima
) {}
