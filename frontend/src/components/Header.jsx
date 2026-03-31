import React, { useState, useEffect } from 'react';
//import '../App.css';
import { useAuth } from '../services/AuthContext';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/Header.css'

const Header = () => {
    const { isAuthenticated, logout, user } = useAuth();
    const navigate = useNavigate();
    const [profilePhoto, setProfilePhoto] = useState(null);

    useEffect(() => {
        // O "user?" evita erro se o objeto estiver nulo enquanto carrega
        if (user?.profilePhoto) {
            setProfilePhoto(user.profilePhoto);
        }
    }, [user]);

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const getInitials = (name) => {
        if(!name) return 'U';
        return name.charAt(0).toUpperCase();
    };

    return (
        <>
        {/* Navbar */}
            <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
                <div className="container">
                    <Link className="navbar-brand fw-bold" to="/">
                        <i className="bi bi-check2-square me-2"></i>
                        KeePace
                    </Link>
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                            <li className="nav-item">
                                <Link className="nav-link" to="/dashboard">
                                    <i className='bi bi-house-door me-1'></i>Home
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to="/listartodos">
                                    <i className='bi bi-list-task me-1'></i>Minhas Tarefas
                                </Link>
                            </li>
                        </ul>
                        {/* Área do usuário */}
                        <div className="d-flex">
                            {!isAuthenticated ? (
                                <>
                                {/* Botões para usuários não logados */}
                            <button className="btn btn-outline-light btn-sm me-2" type="button" title="Login" onClick={() => navigate('/login')}><i className="bi bi-box-arrow-in-right"></i> Login</button>
                            <button className="btn btn-primary btn-sm" type="button" title="Registrar" onClick={() => navigate('/register')}><i className="bi bi-person-check"></i> Registrar</button>
                        </>
                            ) : (
                                     <div className="user-menu">
                                    {/* Avatar com foto ou iniciais */}
                                    <div className="dropdown">
                                        <button 
                                            className="btn btn-link dropdown-toggle user-dropdown" 
                                            type="button" 
                                            data-bs-toggle="dropdown"
                                        >
                                            {profilePhoto ? (
                                                <img 
                                                    src={`http://localhost:8080${profilePhoto}`} 
                                                    alt={user?.name}
                                                    className="user-avatar"
                                                    onError={(e) => {
                                                        e.target.style.display = 'none';
                                                        e.target.parentElement.querySelector('.avatar-initials').style.display = 'flex';
                                                    }}
                                                />
                                            ) : null}
                                            
                                            <div 
                                                className="avatar-initials"
                                                style={{ display: profilePhoto ? 'none' : 'flex' }}
                                                >
                                                {getInitials(user?.name)}
                                            </div>
                                            
                                            <span className="user-name">
                                                {user?.name?.split(' ')[0]}
                                            </span>
                                        </button>
                                        
                                        <ul className="dropdown-menu dropdown-menu-end">
                                            <li>
                                                <Link className="dropdown-item" to="/profile">
                                                    <i className="bi bi-person-circle me-2"></i>
                                                    Meu Perfil
                                                </Link>
                                            </li>
                                            <li>
                                                <Link className="dropdown-item" to="/listartodos">
                                                    <i className="bi bi-list-check me-2"></i>
                                                    Minhas Tarefas
                                                </Link>
                                            </li>
                                            <li>
                                                <Link className="dropdown-item" to="/configuracoes">
                                                    <i className="bi bi-gear me-2"></i>
                                                    Configurações
                                                </Link>
                                            </li>
                                            <li><hr className="dropdown-divider" /></li>
                                            <li>
                                                <button 
                                                    className="dropdown-item text-danger" 
                                                    onClick={handleLogout}
                                                >
                                                    <i className="bi bi-box-arrow-right me-2"></i>
                                                    Sair
                                                </button>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </nav>
            </>
    );
};

export default Header;