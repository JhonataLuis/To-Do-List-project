import React, { useEffect, useState } from 'react';
import api from '../services/api';
import 'bootstrap-icons/font/bootstrap-icons.css'

function TarefaForm({ tarefaParaEditar, onTarefaSalva }) {
    const [formData, setFormData] = useState({
        id: '',
        titulo: '',
        descricao: '',
        concluido: false,
        prioridade: '',
        dataCriacao: '',
        categoria: ''
    });

    // Quando uma tarefa é selecionada para edição, preenche o formulário
    useEffect(() => {
        if (tarefaParaEditar) {
            setFormData({
                ...tarefaParaEditar,
                dataCriacao: tarefaParaEditar.dataCriacao ? tarefaParaEditar.dataCriacao.split('T')[0] : ''
            });
        }
    }, [tarefaParaEditar]);

    const handleChange = (e) => {
        const {id, value, type, checked } = e.target;
        setFormData({
            ...formData,
            [id]: type === 'checkbox' ? checked : value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log("Clicou");

        // Validação
        if (!formData.titulo || !formData.descricao || !formData.prioridade ||
            !formData.categoria) {
                alert('Por favor, preencha todos os campos obrigatórios');
                return;
            }

            console.log(formData);
            try {
                let response;

                // Remove datacriação antes de enviar
                const { dataCriacao, ...dadosSemData } = formData;

                if (formData.id) {
                    // Atualizar tarefa existente
                    response = await api.put(`/tarefas/${formData.id}`, dadosSemData);
                } else {
                    // Criar nova tarefa
                    response = await api.post('/tarefas', dadosSemData);
                }

                onTarefaSalva(response.data);
                resetForm();
            }  catch (error) {
                console.error('Erro ao salvar a tarefa:', error);
                alert('Erro ao salvar tarefa');
            }
    };

    const resetForm = () => {
        setFormData({
            id: '',
            titulo: '',
            descricao: '',
            concluido: false,
            prioridade: '',
            dataCriacao: '',
            categoria: ''
        });
    };

    const handleNovo = () => {
        resetForm();
        onTarefaSalva(null); // Limpa a seleção de edição
    };

    return (
        <div className="form-container">
            <div className="card">
                <div className="card-header text-center">
                    <h4 className="card-title mb-0"> 
                        <i className="bi bi-plus-circle me-2"></i>
                        {formData.id ? 'Editar Tarefa' : 'Cadastro de Tarefas'}
                    </h4>
                </div>
                <div className="card-body">
                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label htmlFor="id" className="form-label">ID</label>
                            <input 
                type="text" 
                className="form-control" 
                id="id" 
                value={formData.id}
                readOnly
              />
            </div>
            <div className="mb-3">
              <label htmlFor="titulo" className="form-label">Título da Tarefa</label>
              <input 
                type="text" 
                className="form-control" 
                id="titulo" 
                placeholder="Digite o título da tarefa" 
                value={formData.titulo}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label htmlFor="descricao" className="form-label">Descrição da Tarefa</label>
              <textarea 
                className="form-control" 
                id="descricao" 
                rows="3" 
                placeholder="Descreva a tarefa"
                value={formData.descricao}
                onChange={handleChange}
                required
              ></textarea>
            </div>
            <div className="mb-3">
              <div className="form-check">
                <input 
                  className="form-check-input" 
                  type="checkbox" 
                  id="concluido"
                  checked={formData.concluido}
                  onChange={handleChange}
                />
                <label className="form-check-label" htmlFor="concluido">
                  Tarefa Concluída
                </label>
              </div>
            </div>
            <div className="mb-3">
              <label htmlFor="prioridade" className="form-label">Prioridade</label>
              <select 
                className="form-select" 
                id="prioridade"
                value={formData.prioridade}
                onChange={handleChange}
                required
              >
                <option value="" disabled>Selecione a prioridade</option>
                <option value="Alta">Alta</option>
                <option value="Media">Média</option>
                <option value="Baixa">Baixa</option>
              </select>
            </div>
            <div className="mb-3">
              <label htmlFor="categoria" className="form-label">Categoria</label>
              <input 
                type="text" 
                className="form-control" 
                id="categoria" 
                placeholder="Ex: Trabalho, Pessoal, Estudos"
                value={formData.categoria}
                onChange={handleChange}
                required
              />
            </div>
            <div className="card-footer bg-transparent border-0 pt-3">
              <div className="d-grid gap-2 d-md-flex justify-content-md-end">
                <button type="submit" className="btn btn-success me-md-2">
                  <i className="bi bi-save me-1"></i>
                  {formData.id ? 'Atualizar Tarefa' : 'Salvar Tarefa'}
                </button>
                <button type="button" className="btn btn-primary" onClick={handleNovo}>
                  <i className="bi bi-plus me-1"></i>Novo
                </button>
              </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default TarefaForm;