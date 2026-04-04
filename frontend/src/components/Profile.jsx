import React, { useState, useEffect } from 'react';
import { useAuth } from '../services/AuthContext';
import api from '../services/api';
import '../styles/Profile.css'

const Profile = () => {
    const { user, updateProfile } = useAuth();
    const [editing, setEditing] = useState(false);
    const [loading, setLoading] = useState(false);
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        currentPassword: '',
        newPassword: '',
        confirmPassword: ''
    });

    const [profilePhoto, setProfilePhoto] = useState(null);
    const [message, setMessage] = useState({ type: '', text: '' });

    useEffect(() => {
        if (user) {
            setFormData({
                ...formData,
                id: user.id,
                name: user.name || '',
                email: user.email || ''
            });
            
            if (user.profilePhoto) {
                setProfilePhoto(user.profilePhoto);
            }
        }
    }, [user]);

    const handleInputChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handlePhotoUpload = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        if (file.size > 5 * 1024 * 1024) {
            setMessage({ type: 'error', text: 'A foto deve ter no máximo 5MB' });
            return;
        }

        const formData = new FormData();
        formData.append('photo', file);

        setLoading(true);
        try {
            const response = await api.post('/users/profile/photo', formData, {
                headers: { 'Content-Type': 'multipart/form-data' }
            });
            
            setProfilePhoto(response.data);
            updateProfile({ ...user, profilePhoto: response.data });
            setMessage({ type: 'success', text: 'Foto atualizada com sucesso!' });
            setTimeout(() => setMessage({ type: '', text: '' }), 3000);
        } catch (error) {
            setMessage({ type: 'danger', text: 'Erro ao atualizar foto' });
        } finally {
            setLoading(false);
        }
    };

    const handleUpdateProfile = async (e) => {
        e.preventDefault();
        setLoading(true);
        
        try {
            //Aqui já faz o api.put e o setUser(update) vindo do AuthContext
            const response = await updateProfile({ name: formData.name });
            
            if(response.success){
                //updateProfile(response.data);
                setEditing(false);
                setMessage({ type: 'success', text: 'Perfil atualizado com sucesso!' });
                setTimeout(() => setMessage({ type: '', text: '' }), 3000);
            } else {
                setMessage({ type: 'danger', text: 'Erro ao atualizar o perfil' });
            }
            
        } catch (error) {
            console.error("Erro detalhado:", error.response?.data || error.message); //teste
            setMessage({ type: 'danger', text: 'Erro inesperado ao atualizar perfil' });
        } finally {
            setLoading(false);
        }
    };

    const handleChangePassword = async (e) => {
        e.preventDefault();
        
        if (formData.newPassword !== formData.confirmPassword) {
            setMessage({ type: 'danger', text: 'As senhas não coincidem' });
            return;
        }
        
        if (formData.newPassword.length < 6) {
            setMessage({ type: 'danger', text: 'A senha deve ter no mínimo 6 caracteres' });
            return;
        }
        
        setLoading(true);
        
        try {
            await api.put('/users/change-password', {
                currentPassword: formData.currentPassword,
                newPassword: formData.newPassword
            });
            
            setFormData({
                ...formData,
                currentPassword: '',
                newPassword: '',
                confirmPassword: ''
            });
            
            setMessage({ type: 'success', text: 'Senha alterada com sucesso!' });
            setTimeout(() => setMessage({ type: '', text: '' }), 3000);
        } catch (error) {
            setMessage({ type: 'danger', text: error.response?.data?.message || 'Erro ao alterar senha' });
        } finally {
            setLoading(false);
        }
    };

    const getInitials = (name) => {
        return name ? name.charAt(0).toUpperCase() : 'U';
    };

    const formatDate = (date) => {
        if (!date) return 'Não disponível';
        return new Date(date).toLocaleDateString('pt-BR');
    };

    return (
        <div className="profile-container">
            <div className="container">
                {/* Mensagem de feedback */}
                {message.text && (
                    <div className={`alert alert-${message.type} alert-dismissible fade show`} role="alert">
                        {message.text}
                        <button type="button" className="btn-close" onClick={() => setMessage({ type: '', text: '' })}></button>
                    </div>
                )}

                <div className="profile-header">
                    <h1 className="profile-title">
                        <i className="bi bi-person-circle me-2"></i>
                        Meu Perfil
                    </h1>
                    <p className="profile-subtitle">Gerencie suas informações pessoais e preferências</p>
                </div>

                <div className="row">
                    {/* Coluna da foto */}
                    <div className="col-md-4 mb-4">
                        <div className="card profile-card">
                            <div className="card-body text-center">
                                <div className="profile-photo-container">
                                    {profilePhoto ? (
                                        <img 
                                            src={`http://localhost:8080${profilePhoto}`} 
                                            alt={user?.name}
                                            className="profile-photo"
                                            onError={(e) => {
                                                e.target.style.display = 'none';
                                                e.target.parentElement.querySelector('.profile-initials').style.display = 'flex';
                                            }}
                                        />
                                    ) : null}
                                    
                                    <div 
                                        className="profile-initials"
                                        style={{ display: profilePhoto ? 'none' : 'flex' }}
                                    >
                                        {getInitials(user?.name)}
                                    </div>
                                    
                                    <label htmlFor="photo-upload" className="photo-upload-btn">
                                        <i className="bi bi-camera-fill"></i>
                                        <span>Trocar foto</span>
                                    </label>
                                    <input
                                        type="file"
                                        id="photo-upload"
                                        accept="image/*"
                                        onChange={handlePhotoUpload}
                                        style={{ display: 'none' }}
                                    />
                                </div>
                                
                                <h3 className="profile-name mt-3">{user?.name}</h3>
                                <p className="profile-email">{user?.email}</p>
                                
                                <div className="profile-badge">
                                    <i className="bi bi-shield-check me-1"></i>
                                    {user?.roles?.includes('ROLE_ADMIN') ? 'Administrador' : 'Usuário'}
                                </div>
                                
                                <div className="profile-info mt-3">
                                    <div className="info-item">
                                        <i className="bi bi-calendar3"></i>
                                        <span>Membro desde: {formatDate(user?.createdAt)}</span>
                                    </div>
                                    <div className="info-item">
                                        <i className="bi bi-envelope"></i>
                                        <span>Email verificado</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Coluna das informações */}
                    <div className="col-md-8">
                        {/* Card de informações pessoais */}
                        <div className="card profile-card mb-4">
                            <div className="card-header d-flex justify-content-between align-items-center">
                                <h4 className="mb-0">
                                    <i className="bi bi-person-badge me-2"></i>
                                    Informações Pessoais
                                </h4>
                                {!editing && (
                                    <button 
                                        className="btn btn-primary btn-sm"
                                        onClick={() => setEditing(true)}
                                    >
                                        <i className="bi bi-pencil me-1"></i>
                                        Editar
                                    </button>
                                )}
                            </div>
                            
                            <div className="card-body">
                                {editing ? (
                                    <form onSubmit={handleUpdateProfile}>
                                        <div className="mb-3">
                                            <label className="form-label">Nome completo</label>
                                            <input
                                                type="text"
                                                className="form-control"
                                                name="name"
                                                value={formData.name}
                                                onChange={handleInputChange}
                                                required
                                            />
                                        </div>
                                        
                                        <div className="mb-3">
                                            <label className="form-label">E-mail</label>
                                            <input
                                                type="email"
                                                className="form-control"
                                                value={formData.email}
                                                disabled
                                            />
                                            <small className="text-muted">O e-mail não pode ser alterado</small>
                                        </div>
                                        
                                        <div className="d-flex gap-2">
                                            <button type="submit" className="btn btn-primary" disabled={loading}>
                                                {loading ? 'Salvando...' : 'Salvar alterações'}
                                            </button>
                                            <button 
                                                type="button" 
                                                className="btn btn-secondary"
                                                onClick={() => {
                                                    setEditing(false);
                                                    setFormData({
                                                        ...formData,
                                                        name: user?.name
                                                    });
                                                }}
                                            >
                                                Cancelar
                                            </button>
                                        </div>
                                    </form>
                                ) : (
                                    <div className="profile-details">
                                        <div className="detail-row">
                                            <div className="detail-label">
                                                <i className="bi bi-person"></i>
                                                Nome
                                            </div>
                                            <div className="detail-value">{user?.name}</div>
                                        </div>
                                        <div className="detail-row">
                                            <div className="detail-label">
                                                <i className="bi bi-envelope"></i>
                                                E-mail
                                            </div>
                                            <div className="detail-value">{user?.email}</div>
                                        </div>
                                        <div className="detail-row">
                                            <div className="detail-label">
                                                <i className="bi bi-shield"></i>
                                                Tipo de conta
                                            </div>
                                            <div className="detail-value">
                                                {user?.roles?.includes('ROLE_ADMIN') ? 'Administrador' : 'Usuário padrão'}
                                            </div>
                                        </div>
                                    </div>
                                )}
                            </div>
                        </div>

                        {/* Card de alterar senha */}
                        <div className="card profile-card">
                            <div className="card-header">
                                <h4 className="mb-0">
                                    <i className="bi bi-key me-2"></i>
                                    Alterar Senha
                                </h4>
                            </div>
                            
                            <div className="card-body">
                                <form onSubmit={handleChangePassword}>
                                    <div className="mb-3">
                                        <label className="form-label">Senha atual</label>
                                        <input
                                            type="password"
                                            className="form-control"
                                            name="currentPassword"
                                            value={formData.currentPassword}
                                            onChange={handleInputChange}
                                            required
                                        />
                                    </div>
                                    
                                    <div className="mb-3">
                                        <label className="form-label">Nova senha</label>
                                        <input
                                            type="password"
                                            className="form-control"
                                            name="newPassword"
                                            value={formData.newPassword}
                                            onChange={handleInputChange}
                                            required
                                            minLength="6"
                                        />
                                    </div>
                                    
                                    <div className="mb-3">
                                        <label className="form-label">Confirmar nova senha</label>
                                        <input
                                            type="password"
                                            className="form-control"
                                            name="confirmPassword"
                                            value={formData.confirmPassword}
                                            onChange={handleInputChange}
                                            required
                                        />
                                    </div>
                                    
                                    <button 
                                        type="submit" 
                                        className="btn btn-warning"
                                        disabled={loading}
                                    >
                                        {loading ? 'Alterando...' : 'Alterar senha'}
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Profile;