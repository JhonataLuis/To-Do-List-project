import React, { createContext, useState, useContext, useEffect } from 'react';
import api from './api';
import '../styles/Header.css'

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if(token) api.defaults.headers.Authorization = `Bearer ${token}`;
    if (token) loadUser();
    else setLoading(false);
  }, []);

  const loadUser = async () => {
    try {
      const { data } = await api.get('/users/profile');
      setUser(data);
    } catch (error) {
      localStorage.removeItem('token');
    } finally {
      setLoading(false);
    }
  };

  const login = async (email, password) => {
    try {
      const { data } = await api.post('/auth/login', { email, password });
      localStorage.setItem('token', data.token);
      api.defaults.headers.Authorization = `Bearer ${data.token}`;
      setUser(data);
      return { success: true };
    } catch (error) {
      return { success: false, error: error.response?.data?.message || 'Login failed' };
    }
  };

  const register = async (name, email, password) => {
    try {
      await api.post('/auth/register', { name, email, password });
      return { success: true };
    } catch (error) {
      return { success: false, error: error.response?.data?.message || 'Registration failed' };
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    setUser(null);
  };

  const updateProfile = async (data) => {
    try {
      const { data: updated } = await api.put('/users/profile', data);
      setUser(updated);
      return { success: true };
    } catch (error) {
      return { success: false, error: 'Update failed' };
    }
  };

  return (
    <AuthContext.Provider value={{
      user, loading, login, register, logout, updateProfile,
      isAuthenticated: !!user,
      isAdmin: user?.roles?.includes('ROLE_ADMIN')
    }}>
      {children}
    </AuthContext.Provider>
  );
};