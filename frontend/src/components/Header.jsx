import React from 'react';
import '../App.css';

const Header = () => {
    return (
        <>
        {/* Navbar */}
            <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
                <div class="container">
                    <a class="navbar-brand fw-bold" href="index.html">
                        <i class="bi bi-check2-square me-2"></i>ToDo List
                    </a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                            <li class="nav-item">
                                <a class="nav-link active" aria-current="page" href=".html">Home</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="/listartodos">Minhas Tarefas</a>
                            </li>
                        </ul>
                        <div className="d-flex">
                            <button className="btn btn-outline-light btn-sm me-2" type="button" title="Login"><i class="bi bi-box-arrow-in-right"></i> Login</button>
                            <button className="btn btn-primary btn-sm" type="button" title="Registrar"><i class="bi bi-person-check"></i> Registrar</button>
                        </div>
                    </div>
                </div>
            </nav>
            </>
    );
};

export default Header;