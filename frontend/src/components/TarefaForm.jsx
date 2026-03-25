import React, { useState } from 'react';
import api from '../services/api';

function TarefaForm({ onTarefaAdicionada }){
    const [titulo, setTitulo] = useState('');
    const [descricao, setDescricao] = useState('');
    const [submetendo, setSubmetendo] = (false);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!titulo.trim()) {
            alert("Título é obrigatório");
            return;
        }

        try {
            setSubmetendo(true);
            const novaTarefa = {
                titulo: titulo,
                descricao: descricao, 
                concluido: false
            };

            const response = await api.post('/tarefas', novaTarefa);
            onTarefaAdicionada(response.data);

            // Limpar formulário
            setTitulo('');
            setDescricao('');

        } catch (error) {
            console.error('Erro ao criar tarefa:', error);
            alert('Erro ao criar tarefa');
        } finally {
            setSubmetendo(false);
        }
    };

    return (
        <form className="tarefa-form" onSubmit={handleSubmit}>
            <div className="form-group">
                <input type="text" value={titulo} onChange={(e) => setTitulo(e.target.value)}
                placeholder="Título da tarefa" 
                disabled={submetendo} 
                />
            </div>
            <div className="form-group">
                <textarea value={descricao} onChange={(e) => setDescricao(e.target.value)}
                placeholder="Descrição da tarefa"
                disabled={submetendo}
                />
                <button type="submit" disabled={submetendo}>
                    {submetendo ? 'Adicionando...' : 'Adicionar Tarefa'}
                </button>
            </div>
        </form>
    )
}

export default TarefaForm;