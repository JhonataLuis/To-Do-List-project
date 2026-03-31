import React from 'react';
import '../App.css';
import { useAuth } from '../services/AuthContext';
import { Link, useNavigate } from 'react-router-dom';

const Header = () => {
    const { isAuthenticated, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <>
        {/* Navbar */}
            <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
                <div className="container">
                    <a className="navbar-brand fw-bold" href="/">
                        <i className="bi bi-check2-square me-2"></i>KeePace
                    </a>
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                            <li className="nav-item">
                                <a className="nav-link active" aria-current="page" href=".html">Home</a>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to="/listartodos">Minhas Tarefas</Link>
                            </li>
                        </ul>
                        <div className="d-flex">
                            {!isAuthenticated ? (
                                <>
                                {/* Botões para usuários não logados */}
                            <button className="btn btn-outline-light btn-sm me-2" type="button" title="Login" onClick={() => navigate('/login')}><i className="bi bi-box-arrow-in-right"></i> Login</button>
                            <button className="btn btn-primary btn-sm" type="button" title="Registrar" onClick={() => navigate('/register')}><i className="bi bi-person-check"></i> Registrar</button>
                        </>
                            ) : (
                                /* Botão para usuários LOGADOS */
                                <button 
                                className="btn btn-danger btn-sm" 
                                onClick={handleLogout}
                            >
                                <i className="bi bi-box-arrow-left"></i> Sair
                            </button>
                            )}
                        </div>
                    </div>
                </div>
            </nav>
            </>
    );
};

export default Header;