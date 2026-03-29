import React, { useState } from 'react';
import { useAuth } from '../services/AuthContext'; // Ajuste o caminho se necessário
import { useNavigate } from 'react-router-dom';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    const result = await login(email, password);

    if (result.success) {
      navigate('/'); // Redireciona para a home após login
    } else {
      setError(result.error || 'E-mail ou senha incorretos.');
    }
    setLoading(false);
  };

  return (
    <div className="container d-flex justify-content-center align-items-center vh-100">
      <div className="card shadow-lg p-4" style={{ maxWidth: '400px', width: '100%' }}>
        <div className="text-center mb-4">
          <i className="fas fa-tasks fa-3x text-primary mb-3"></i>
          <h2 className="fw-bold">KeePace</h2>
          <p className="text-muted">Entre na sua conta</p>
        </div>

        {error && (
          <div className="alert alert-danger py-2" role="alert">
            <small>{error}</small>
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="form-label">E-mail</label>
            <div className="input-group">
              <span className="input-group-text"><i className="fas fa-envelope"></i></span>
              <input
                type="email"
                className="form-control"
                placeholder="exemplo@email.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
          </div>

          <div className="mb-4">
            <label className="form-label">Senha</label>
            <div className="input-group">
              <span className="input-group-text"><i className="fas fa-lock"></i></span>
              <input
                type="password"
                className="form-control"
                placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>
          </div>

          <button 
            type="submit" 
            className="btn btn-primary w-100 py-2 fw-bold"
            disabled={loading}
          >
            {loading ? (
              <span className="spinner-border spinner-border-sm me-2"></span>
            ) : 'Entrar'}
          </button>
        </form>

        <div className="text-center mt-4">
          <small className="text-muted">
            Não tem uma conta? <a href="/register" className="text-decoration-none">Cadastre-se</a>
          </small>
        </div>
      </div>
    </div>
  );
};

export default Login;