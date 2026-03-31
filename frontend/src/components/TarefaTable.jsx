import React from 'react';
import ReactPaginate from 'react-paginate'; // react-paginate
import api from '../services/api';


function TarefaTable({ tarefas, onEditar, onTarefaExcluida, pageCount, onPageChange }) {
  
  const getPriorityClass = (prioridade) => {
    switch(prioridade) {
      case 'Alta':
        return 'priority-high';
      case 'Media':
        return 'priority-medium';
      case 'Baixa':
        return 'priority-low';
      default:
        return '';
    }
  };

  const handleExcluir = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir esta tarefa?')) {
      try {
        await api.delete(`/tasks/tarefas/${id}`);
        onTarefaExcluida(id);
      } catch (error) {
        console.error('Erro ao excluir tarefa:', error);
        alert('Erro ao excluir tarefa');
      }
    }
  };

  // Função para informar se a task está em dia ou atrasada
  const getStatusData = (dueDate) => {
    if (!dueDate) return {classe: '', texto: ''};

    const hoje = new Date();
    hoje.setHours(0,0,0,0); // Zera as horas para comparar apenas os dias

    // Converte o prazo que veio do banco para Data e zeramos a hora
    const dataPrazo = new Date(dueDate);
    dataPrazo.setHours(0,0,0,0);

    // Adiciona um dia para compensar o fuso horário se necessário (Opcional depende do banco)
    dataPrazo.setDate(dataPrazo.getDate() +1);

    if(dataPrazo < hoje){
      return { classe: 'badge bg-danger', texto: 'Atrasada' };
    } else if (dataPrazo.getTime() === hoje.getTime()){
      return { classe: 'badge bg-warning text-dark', texto: 'Para Hoje' };
    } else {
      return { classe: 'badge bg-info', texto: 'No Prazo' };
    }
  };

  const handleToggleStatus = async (tarefa) => {
    try {
      const tarefaAtualizada = {
        ...tarefa,
        concluido: !tarefa.concluido
      };
      const response = await api.put(`/tasks/tarefas/${tarefa.id}`, tarefaAtualizada);
      onTarefaExcluida(tarefa.id, response.data); // Atualiza na lista
    } catch (error) {
      console.error('Erro ao atualizar status:', error);
    }
  };

  const formatarData = (data) => {
    if (!data) return '-';
    return new Date(data).toLocaleDateString('pt-BR');
  };

  return (
    <div className="table-container">
      <div className="table-title">
        <h5 className="mb-0">
          <i className="bi bi-list me-2"></i>Lista de Tarefas
        </h5>
      </div>
      <div className="table-responsive">
        <table className="table table-hover" id="tabelaResultados">
          <thead>
            <tr>
              <th scope="col">ID</th>
              <th scope="col">Status</th>
              <th scope="col">Título</th>
              <th scope="col">Descrição</th>
              <th scope="col">Prioridade</th>
              <th scope='col'>Data Entrega</th>
              <th scope="col">Categoria</th>
              <th scope="col">Data Criação</th>
              <th scope='col'>Update At</th>
              <th scope="col" className="text-center">Ações</th>
            </tr>
          </thead>
          <tbody>
            {tarefas.length === 0 ? (
              <tr>
                <td colSpan="8" className="text-center py-4">
                    <i className="bi bi-clipboard-x" style={{ fontSize: "2rem", color: "blue"}}></i>
                    <p className="mt-2 bm-0">Nenhuma tarefa cadastrada</p>
                </td>
              </tr>
            ) : (
              tarefas.map((tarefa) => {
                const statusDueDate = getStatusData(tarefa.dueDate);
                return (
                <tr key={tarefa.id} style={{ 
                  textDecoration: tarefa.concluido ? 'line-through' : 'none',
                  opacity: tarefa.concluido ? 0.7 : 1
                }}>
                  <td>{tarefa.id}</td>
                  <td>
                    <div className="form-check">
                      <input 
                        className="form-check-input" 
                        type="checkbox"
                        checked={tarefa.concluido}
                        onChange={() => handleToggleStatus(tarefa)}
                        style={{ cursor: 'pointer' }}
                      />
                    </div>
                  </td>
                  <td>{tarefa.titulo}</td>
                  <td>{tarefa.descricao}</td>
                  <td>
                    <span className={`priority-badge ${getPriorityClass(tarefa.prioridade)}`}>
                      {tarefa.prioridade}
                    </span>
                  </td>
                  <td>
                    {formatarData(tarefa.dueDate)}
                    <br/>
                      <span className={statusDueDate.classe} style={{fontSize: '0.7rem'}}>
                        {statusDueDate.texto}
                      </span>
                  </td>
                  <td>{tarefa.categoria}</td>
                  <td>{formatarData(tarefa.dataCriacao)}</td>
                  <td>{formatarData(tarefa.updatedAt)}</td>
                  <td className="text-center">
                  <div className="d-flex justify-content-center gap-2">
                        <button 
                        className="btn btn-warning btn-sm"
                        onClick={() => onEditar(tarefa)}
                        title="Editar"
                        >
                        <i className="bi bi-pencil"></i>
                        </button>
                        <button 
                        className="btn btn-danger btn-sm"
                        onClick={() => handleExcluir(tarefa.id)}
                        title="Excluir"
                        >
                        <i className="bi bi-trash"></i>
                        </button>
                    </div>
                  </td>
                </tr>
              );
              })
            )}
          </tbody>
        </table>
      </div>

      <div className="d-flex justify-content-center mt-4">
        {pageCount > 0 && (
        <ReactPaginate
            previousLabel={<span><i className="bi bi-arrow-left-circle"> Anterior</i></span>}
            nextLabel={<span>Próximo <i className='bi bi-arrow-right-circle'></i></span>}
            pageCount={pageCount}
            onPageChange={onPageChange}
            containerClassName={'pagination justify-content-center mt-3'}
            pageClassName={'page-item'}
            pageLinkClassName={'page-link'}
            previousClassName={'page-item'}
            previousLinkClassName={'page-link'}
            nextClassName={'page-link'}
            activeClassName={'active'}
        />
        )}
        </div>
    </div>
  );
}

export default TarefaTable;