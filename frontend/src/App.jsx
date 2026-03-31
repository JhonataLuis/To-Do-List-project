import React, { useState, useEffect, Children } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import './App.css';
import TarefaForm from './components/TarefaForm';
import TarefaTable from './components/TarefaTable';
import Header from './components/Header';
import Footer from './components/Footer';
import api from './services/api';
import { AuthProvider, useAuth } from './services/AuthContext';
import Login from './components/Login';
import Register from './components/Register';
import Profile from './components/Profile';
import LandingPage from './components/LandingPage';

const PrivateRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();

  if (loading) return <div className="text-center mt-5">Carregando autenticação...</div>;

  return isAuthenticated ? children : <Navigate to="/login" />
}

// Componente Painel de Tarefas
const Dashboard = ({
  tarefas, handleEditar, handleTarefaExcluida, pageCount, handlePageChange,
  tarefaEditando, handleTarefaSalva, error, setError
}) => {
  return (
    <div className="container-fluid main-container mt-4">
      {error && (
        <div className="alert alert-danger alert-dismissible fade show" role="alert">
          <i className="fas fa-exclamation-circle me-2"></i>
          {error}
          <button type="button" className="btn-close" onClick={() => setError(null)}></button>
        </div>
      )}
      <div className="row">
        <div className="col-lg-5 col-xl-4">
          <TarefaForm 
          tarefaParaEditar={tarefaEditando} 
          onTarefaSalva={handleTarefaSalva} />
        </div>
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
  );
}

function AppContent() {

  const { isAuthenticated } = useAuth();

  const [tarefas, setTarefas] = useState([]);
  const [tarefaEditando, setTarefaEditando] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(0);
  const [size] = useState(5);
  const [pageCount, setPageCount] = useState(0);

  // Carregar tarefas ao iniciar
  useEffect(() => {
    if (isAuthenticated){
      carregarTarefas(page);
    }
  }, [page, isAuthenticated]);

  const carregarTarefas = async (pageAtual = 0) => {
    try {
      setLoading(true);
      const response = await api.get(`/tasks/tarefas/paginadas?page=${pageAtual}&size=${size}`);
      setTarefas(response.data.content || []); // Lista
      const totalPages = response.data.page?.totalPages || 0; // O total de paginas agora vem dentro do objeto 'page'
      setPageCount(totalPages); // Total de páginas
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


  return (
    <div className="App d-flex flex-column min-vh-100">
      {/* Navigation */}
      <AuthProvider> {/* O Provider deve envolver Tudo */}
     
         <Header /> {/* O Header deve estar aqui dentro */}

         <main className="flex-grow-1">
          <Routes>
            {/* Primeira página que o usuário vê (Home) */}
            <Route path='/' element={<LandingPage />}/>

            {/* Paginas de Autenticação */}
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />

            {/* Rota que exibe o formulário e a lista tasks */}
            <Route
              path='/listartodos'
              element={
                isAuthenticated ? (
                  <div className="container-fluid mt-4">
                    <div className="row">
                      <div className="col-md-4">
                        <TarefaForm 
                          tarefaParaEditar={tarefaEditando}
                          onTarefaSalva={() => carregarTarefas(page)} 
                        />
                      </div>
                      <div className="col-md-8">
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
                ) : (
                  <Navigate to="/login" />
                )
              } 
            />

            {/* Pagina Principal de Tarefas (Protegida) */}
            <Route path="/dashboard" element={
              <PrivateRoute>
                 {loading ? (
                        <div className="container-fluid main-container">
                          <div className="text-center py-5">
                            <div className="spinner-border text-primary" role="status">
                              <span className="visually-hidden">Carregando...</span>
                            </div>
                            <p className="mt-3">Carregando tarefas...</p>
                          </div>
                        </div>
                  ) : (
                <Dashboard 
                tarefas={tarefas}
                      handleEditar={handleEditar}
                      handleTarefaExcluida={handleTarefaExcluida}
                      pageCount={pageCount}
                      handlePageChange={handlePageChange}
                      tarefaEditando={tarefaEditando}
                      handleTarefaSalva={handleTarefaSalva}
                      error={error}
                      setError={setError}
                      />
                  )}
                </PrivateRoute>
            } />
            {/* Redireciona quaquer erro para a Home */}
            <Route path='*' element={<Navigate to="/" />} />
            <Route path="/profile" element={
              <PrivateRoute><Profile /></PrivateRoute>
            } />
            <Route path="/" element={<Navigate to="/dashboard" />} />
          </Routes>
        </main>
        <Footer />
        
    </AuthProvider>

      <div className="container-fluid main-container">
        {error && (
          <div className="alert alert-danger alert-dismissible fade show" role="alert">
            <i className="fas fa-exclamation-circle me-2"></i>
            {error}
            <button type="button" className="btn-close" onClick={() => setError(null)}></button>
          </div>
        )}
      </div>
    </div>
  );
}

// O AppContent principal provicencia o Contexto e o Router
function App() {
return (
    <AuthProvider>
      <BrowserRouter>
        <AppContent />
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;