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
                    <button onClick={handleUpdate}>Salvar</button>
                    <button onClick={() => setEditando(false)}>Cancelar</button>
            </div>
        );
    }

    return (
        <div className={"tarefa-item ${tarefa.concluido ? 'concluido' : ''}"}>
            <div className="tarefa-content">
                <input
                    type="checkbox"
                    checked={tarefa.concluido}
                    onChange={toggleConcluido}
                    />
                    <div className="tarefa-info">
                        <h3>${tarefa.titulo}</h3>
                        <p>${tarefa.descricao}</p>
                    </div>
            </div>
            <div className="tarefa-actions">
                <button onClick={() => setEditando(true)}>Editar</button>
                <button onClick={handleDelete}>Excluir</button>
            </div>
        </div>
    );
}

export default TarefItem;