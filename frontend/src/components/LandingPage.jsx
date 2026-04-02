import React from 'react';
import { useAuth } from '../services/AuthContext';
import { Link, Navigate } from 'react-router-dom';

const LandingPage = () => {

    const { isAuthenticate} = useAuth();

    // Se já estiver logado, vai direto para as tarefas
    if (isAuthenticate) {
        return <Navigate to="/dashboard" />
    }
    
    return (
        <div className="bg-light vh-100">
            {/* Hero Section */}
            <header className="py-5 bg-dark text-white text-center">
                <div className="container">
                    <h1 className="display-4 fw-bold">Bem-vindo ao KeePace</h1>
                    <p className="lead">Organize suas tarefas de forma simples e rápida.</p>
                    {!isAuthenticate ? (
                        // Se estiver LOGADO, mostra isso:
                        <Link to="/dashboard" className="btn btn-success btn-lg">
                            <i className="bi bi-speedometer2 me-2"></i>Acessar meu Painel
                        </Link>
                        ) : (
                            // Se NÃO estiver logado, mostra os botões originais:
                            <>
                            <Link to="/login" className="btn btn-primary btn-lg me-3">
                                <i className="bi bi-box-arrow-in-right me-2"></i>Entrar
                            </Link>
                            <Link to="/register" className="btn btn-outline-light btn-sm">
                                Criar conta gratuita
                            </Link>
                        </>
                    )}
                </div>
            </header>

            {/* Features Section */}
            <section className="container py-5 mt-5">
                <div className="text-center mb-5">
                    <h2 className="fw-bold">Por que usar o KeePace?</h2>
                    <p className="text-muted">A ferramenta ideal para organizar sua rotina profissional e pessoal.</p>
                </div>

                <div className="row g-4 text-center">
                    {/* Gestão Inteligente */}
                    <div className="col-md-4">
                        <div className="card h-100 border-0 shadow-sm p-4 hover-shadow">
                            <div className="card-body">
                                <div className="icon-box mb-3 bg-primary-subtle d-inline-block p-3 rounded-circle">
                                    <i className="bi bi-layout-text-sidebar-reverse display-6 text-primary"></i>
                                </div>
                                <h4 className="fw-bold mb-3">Gestão de Tarefas</h4>
                                <p className="text-muted">Crie, edite e organize suas tarefas com prioridades e categorias customizáveis.</p>
                            </div>
                        </div>
                    </div>

                    {/* Segurança de Dados */}
                    <div className="col-md-4">
                        <div className="card h-100 border-0 shadow-sm p-4 hover-shadow">
                            <div className="card-body">
                                <div className="icon-box mb-3 bg-success-subtle d-inline-block p-3 rounded-circle">
                                    <i className="bi bi-shield-lock display-6 text-success"></i>
                                </div>
                                <h4 className="fw-bold mb-3">Segurança Total</h4>
                                <p className="text-muted">Autenticação de ponta a ponta. Seus dados são criptografados e acessíveis apenas por você.</p>
                            </div>
                        </div>
                    </div>

                    {/* Produtividade */}
                    <div className="col-md-4">
                        <div className="card h-100 border-0 shadow-sm p-4 hover-shadow">
                            <div className="card-body">
                                <div className="icon-box mb-3 bg-warning-subtle d-inline-block p-3 rounded-circle">
                                    <i className="bi bi-graph-up-arrow display-6 text-warning"></i>
                                </div>
                                <h4 className="fw-bold mb-3">Acompanhe sua Evolução</h4>
                                <p className="text-muted">Visualize seu progresso diário e mantenha o foco no que realmente importa.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    );
};

export default LandingPage;