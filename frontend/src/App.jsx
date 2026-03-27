import React, { useState, useEffect } from 'react';
import './App.css';
import TarefaForm from './components/TarefaForm';
import TarefaTable from './components/TarefaTable';
import api from './services/api';

function App() {
  const [tarefas, setTarefas] = useState([]);
  const [tarefaEditando, setTarefaEditando] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(0);
  const [size] = useState(5);
  const [pageCount, setPageCount] = useState(0);

  // Carregar tarefas ao iniciar
  useEffect(() => {
    carregarTarefas(page);
  }, [page]);

  const carregarTarefas = async (pageAtual = 0) => {
    try {
      setLoading(true);
      const response = await api.get(`/tarefas/paginadas?page=${pageAtual}&size=${size}`);
      setTarefas(response.data.content); // Lista
      setPageCount(response.data.totalPages); // Total de páginas
      setError(null);
    } catch (err) {
      console.error('Erro ao carregar tarefas:', err);
      setError('Erro ao carregar tarefas. Verifique se o backend está rodando.');
    } finally {
      setLoading(false);
    }
  };

  const handlePageChange = (data) => {
    setPage(data.selected);
  };

  const handleTarefaSalva = (tarefaSalva) => {
    if (tarefaEditando) {
      // Atualizar na lista
      setTarefas(tarefas.map(t => t.id === tarefaSalva.id ? tarefaSalva : t));
      setTarefaEditando(null);
    } else {
      // Adicionar nova tarefa
      setTarefas([...tarefas, tarefaSalva]);
    }
  };

  const handleEditar = (tarefa) => {
    setTarefaEditando(tarefa);
    // Rolar para o topo do formulário
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleTarefaExcluida = (id, tarefaAtualizada = null) => {
    if (tarefaAtualizada) {
      // Atualizar tarefa (toggle status)
      setTarefas(tarefas.map(t => t.id === id ? tarefaAtualizada : t));
    } else {
      // Remover tarefa
      setTarefas(tarefas.filter(t => t.id !== id));
      if (tarefaEditando && tarefaEditando.id === id) {
        setTarefaEditando(null);
      }
    }
  };

  if (loading) {
    return (
      <div className="container-fluid main-container">
        <div className="text-center py-5">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Carregando...</span>
          </div>
          <p className="mt-3">Carregando tarefas...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="App">
      {/* Navigation */}
      <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
        <div className="container">
            <a className="navbar-brand fw-bold" href="index.html">
                <i className="bi bi-list-task me-2"></i>ToDo List
            </a>
            <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>
            <div className="collapse navbar-collapse" id="navbarSupportedContent">
                <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                    <li className="nav-item">
                        <a className="nav-link active" aria-current="page" href="index.html">Home</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" href="tarefas.html">Minhas Tarefas</a>
                    </li>
                </ul>
                <div className="d-flex">
                    <button className="btn btn-outline-light btn-sm me-2" type="button" title="Login"><i class="bi bi-box-arrow-in-right"></i> Login</button>
                    <button className="btn btn-primary btn-sm" type="button" title="Registrar"><i class="bi bi-person-check"></i> Registrar</button>
                </div>
            </div>
        </div>
    </nav>

      <div className="container-fluid main-container">
        {error && (
          <div className="alert alert-danger alert-dismissible fade show" role="alert">
            <i className="fas fa-exclamation-circle me-2"></i>
            {error}
            <button type="button" className="btn-close" onClick={() => setError(null)}></button>
          </div>
        )}
        
        <div className="row">
          {/* Form Column */}
          <div className="col-lg-5 col-xl-4">
            <TarefaForm 
              tarefaParaEditar={tarefaEditando}
              onTarefaSalva={handleTarefaSalva}
            />
          </div>
          
          {/* Table Column */}
          <div className="col-lg-7 col-xl-8">
            <TarefaTable 
              tarefas={tarefas}
              onEditar={handleEditar}
              onTarefaExcluida={handleTarefaExcluida}
              pageCount={pageCount}
              onPageChange={handlePageChange}
            />
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;