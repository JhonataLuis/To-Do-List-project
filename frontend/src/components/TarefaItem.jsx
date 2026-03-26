import React, { useState } from 'react';
import api from '../services/api';

function TarefaItem({ tarefa, onAtualizar, onRemover }) {
    const [editando, setEditando] = usetState(false);
    const [tituloEditado, setTituloEditado] = useState(tarefa.titulo);
    const [descricaoEditada, setDescricaoEditada] = useState(tarefa.descricao);

    const handleDelete = async () => {
        if (window.confirm('Tem certeza que deseja excluir esta tarefa?')) {
            try {
                await api.delete('/tarefas/${tarefa.id}');
                orRemover(tarefa.id);
            } catch (error) {
                console.error('Erro ao deletar tarefa:', error);
            }
        }
    };

    const handleUpdate = async () => {
        try {
            const tarefaAtualizada = {
                ...tarefa,
                titulo: tituloEditado,
                descricao: descricaoEditada
            };
            
            const response = await api.put('/tarefas/${tarefa.id}', tarefaAtualizada);
            onAtualizar(response.data);
            setEditando(false);
        } catch (error) {
            console.error('Erro ao atualizar tarefa:', error);
        }
    };

    const toggleConcluido = async () => {
        try {
            const tarefaAtualizada = {
                ...tarefa,
                concluido: !tarefa.concluido
            };

            const response = await api.put('/tarefas/${tarefa.id}', tarefaAtualizada);
            onAtualizar(response.data);

        } catch (error) {
            console.error('Erro ao atualizar status:', error);
        }
    };

    if (editando){
        return (
            <div className="tarefa-item editing">
                <input
                    type="text"
                    value={tituloEditado}
                    onChange={(e) => setTituloEditando(e.target.value)}
                    placeholder="Título"
                    />
            </div>
        )
    }
}