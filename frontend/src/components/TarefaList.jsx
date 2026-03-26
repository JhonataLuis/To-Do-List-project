import React, { useState, useEffect } from 'react';
import api from '../services/api';
import TarefaForm from './TarefaForm';

function TarefaList() {
    const [tarefas, setTarefas] = useState([]);
    const [loading, setLoading] = usetState(true);
    const [error, setError] = useState(null);

    // Carregar tarefas ao montar o componente
    useEffect(() => {
        carregarTarefas();
    }, []);

    const carregarTarefas = async () => {
        try{
            setLoading(true);
            const response = await api.get('/listartodos');
            setTarefas(response.data);
            setError(null);
        } catch  (err) {
            setError('Erro ao carregar as tarefas');
            console.error('Erro ao carregar as tarefas', err);
        } finally {
            setLoading(false);
        }
    };

    const adicionarTarefa = (novaTarefa) => {
        setTarefas([...tarefas, novaTarefa]);
    };

    const atualizarTarefa = (tarefaAtualizada) => {
        setTarefas(tarefas.map(tarefa => 
            tarefa.id === tarefaAtualizada.id ? tarefaAtualizada : tarefa
        ));
    };

    const removerTarefa = (id) => {
        setTarefas(tarefas.filter(tarefa => tarefa.id !== id));
    };

    if (loading) return <div className="loading">Carregando...</div>
    if (error) return <div className="error">{error}</div>;

    return (
        <div className="tarefa-container">
            <h2>Lista de Tarefas</h2>
            <tarefaForm onTarefaAdicionada={adicionarTarefa} />

            <div className="tarefas-list">
                {tarefas.lenght === 0 ? (
                    <p>Nenhuma tarefa cadastrada</p>
                ) : (
                    tarefas.map(tarefa => (
                        <TarefaItem key={tarefa.id} tarefa={tarefa}
                        onAtualizar={atualizarTarefa}
                        onRemover={removerTarefa}
                        />
                    ))
                )}
            </div>
        </div>
    );
}

export default TarefaList;