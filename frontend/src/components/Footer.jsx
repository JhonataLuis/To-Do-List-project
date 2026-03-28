import React from 'react';
import '../styles/Footer.css';

const Footer = () => {
    return (
        <footer>
            <div className="container">
                <div className="row">
                <div className="col-md-6">
                    <h5>KeePace</h5>
                    <p>Uma forma simples e eficiente de organizar suas tarefas diárias.</p>
                </div>
                <div className="col-md-3">
                    <h5>Links</h5>
                    <ul className="list-unstyled">
                    <li><a href="/" className="text-decoration-none">Home</a></li>
                    <li><a href="/tarefas" className="text-decoration-none">Minhas Tarefas</a></li>
                    <li><a href="/sobre" className="text-decoration-none">Sobre</a></li>
                    </ul>
                </div>
                <div className="col-md-3">
                    <h5>Contato</h5>
                    <ul className="list-unstyled">
                    <li><i className="bi bi-envelope me-2"></i>contato@keepace.com</li>
                    <li><i className="bi bi-telephone me-2"></i>(11) 99999-9999</li>
                    </ul>
                </div>
                </div>
                <hr className="my-4" />
                <div className="text-center">
                <p>&copy; 2026 KeePace. Todos os direitos reservados.</p>
                </div>
            </div>
        </footer>
    );
};

export default Footer;