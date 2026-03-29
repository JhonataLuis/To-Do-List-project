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
                    <div className="mt-4">
                        <Link to="/login" className="btn btn-primary btn-lg me-3">
                            <i className="bi bi-box-arrow-in-right me-2"></i>Entrar
                        </Link>
                        <Link to="/register" className="btn btn-outline-light btn-sm">
                            Criar conta gratuita
                        </Link>
                    </div>
                </div>
            </header>

            {/* Features Section */}
            <section className="container py-5">
                <div className="row text-center">
                    <div className="col-md-4">
                        <div className="p-3">
                            <i className="bi bi-shield-check display-5 text-primary"></i>
                            <h3 className="mt-3">Segurança JWT</h3>
                            <p className="text-muted">Seus dados protegidos com a tecnologia Spring Security.</p>
                        </div>
                    </div>
                    <div className="col-md-4">
                        <div className="p-3">
                            <i className="bi bi-lightning-charge display-5 text-warning"></i>
                            <h3 className="mt-3">Agilidade</h3>
                            <p className="text-muted">Interface rápida e intuitiva para não perder tempo.</p>
                        </div>
                    </div>
                    <div className="col-md-4">
                        <div className="p-3">
                            <i className="bi bi-cloud-check display-5 text-success"></i>
                            <h3 className="mt-3">Sincronizado</h3>
                            <p className="text-muted">Acesse suas tarefas de qualquer lugar.</p>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    );
};

export default LandingPage;