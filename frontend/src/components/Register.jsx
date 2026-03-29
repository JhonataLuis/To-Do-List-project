import React, { useState } from 'react';
import { useAuth } from '../services/AuthContext';
import { useNavigate, Link } from 'react-router-dom';

const Register = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const { register } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (password !== confirmPassword) {
      return setError('As senhas não coincidem.');
    }

    setLoading(true);
    const result = await register(name, email, password);

    if (result.success) {
      // Cadastro ok, envia para o login
      alert('Cadastro realizado com sucesso! Faça seu login.');
      navigate('/login');
    } else {
      setError(result.error || 'Erro ao cadastrar usuário.');
    }
    setLoading(false);
  };

  return (
    <div className="container d-flex justify-content-center align-items-center vh-100">
      <div className="card shadow-lg p-4" style={{ maxWidth: '450px', width: '100%' }}>
        <div className="text-center mb-4">
          <h2 className="fw-bold">Criar Conta</h2>
          <p className="text-muted">Junte-se ao nosso KeePace</p>
        </div>

        {error && <div className="alert alert-danger py-2"><small>{error}</small></div>}

        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="form-label">Nome Completo</label>
            <input
              type="text"
              className="form-control"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">E-mail</label>
            <input
              type="email"
              className="form-control"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <div className="row">
            <div className="col-md-6 mb-3">
              <label className="form-label">Senha</label>
              <input
                type="password"
                className="form-control"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>
            <div className="col-md-6 mb-3">
              <label className="form-label">Confirmar</label>
              <input
                type="password"
                className="form-control"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
              />
            </div>
          </div>

          <button type="submit" className="btn btn-success w-100 py-2 fw-bold" disabled={loading}>
            {loading ? <span className="spinner-border spinner-border-sm me-2"></span> : 'Cadastrar'}
          </button>
        </form>

        <div className="text-center mt-4">
          <small className="text-muted">
            Já tem uma conta? <Link to="/login" className="text-decoration-none">Entrar agora</Link>
          </small>
        </div>
      </div>
    </div>
  );
};

export default Register;