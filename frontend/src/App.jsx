import React, { useState, useEffect } from 'react';
import './App.css';
import TarefaForm from './components/TarefaForm';
import TarefaTable from './components/TarefaTable';
import Header from './components/Header';
import Footer from './components/Footer';
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
      <>
      <Header/>
      <div className="container-fluid main-container">
        <div className="text-center py-5">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Carregando...</span>
          </div>
          <p className="mt-3">Carregando tarefas...</p>
        </div>
      </div>
      <Footer/>
      </>
    );
  }

  return (
    <div className="App d-flex flex-column min-vh-100">
      {/* Navigation */}
      <Header />

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
      <Footer/>
    </div>
  );
}

export default App;