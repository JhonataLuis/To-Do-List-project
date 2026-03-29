import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const result = await login(email, password);
    if (result.success) navigate('/dashboard');
    else setError(result.error);
  };

  return (
    <div className="container mt-5" style={{ maxWidth: '400px' }}>
      <div className="card">
        <div className="card-body">
          <h2 className="text-center mb-4">Login</h2>
          {error && <div className="alert alert-danger">{error}</div>}
          <form onSubmit={handleSubmit}>
            <input type="email" className="form-control mb-2" placeholder="Email"
                   value={email} onChange={e => setEmail(e.target.value)} required />
            <input type="password" className="form-control mb-3" placeholder="Password"
                   value={password} onChange={e => setPassword(e.target.value)} required />
            <button type="submit" className="btn btn-primary w-100 mb-2">Login</button>
            <Link to="/forgot-password" className="d-block text-center">Forgot password?</Link>
            <Link to="/register" className="d-block text-center mt-2">Create account</Link>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Login;