import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { Link, useNavigate } from 'react-router-dom';
import { Mail, Lock, ArrowRight, User, Stethoscope, Building, FileBadge } from 'lucide-react';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import './Auth.css';

const SignupPage = () => {
    const [role, setRole] = useState('patient');
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        // Mock signup
        if (role === 'patient') {
            navigate('/client');
        } else {
            navigate('/pharmacy');
        }
    };

    return (
        <div className="auth-page">
            <Navbar />
            <div className="auth-container">
                <motion.div
                    className="auth-card"
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5 }}
                >
                    <div className="auth-header">
                        <h2>Create Account</h2>
                        <p>Join PharmaConnect today</p>
                    </div>

                    <div className="role-selector">
                        <button
                            className={`role-btn ${role === 'patient' ? 'active' : ''}`}
                            onClick={() => setRole('patient')}
                        >
                            <User size={18} />
                            Patient
                        </button>
                        <button
                            className={`role-btn ${role === 'pharmacy' ? 'active' : ''}`}
                            onClick={() => setRole('pharmacy')}
                        >
                            <Stethoscope size={18} />
                            Pharmacy
                        </button>
                    </div>

                    <form onSubmit={handleSubmit} className="auth-form">
                        <div className="form-group">
                            <label>Full Name</label>
                            <div className="input-wrapper">
                                <User size={18} className="input-icon" />
                                <input type="text" placeholder="John Doe" required />
                            </div>
                        </div>

                        {role === 'pharmacy' && (
                            <div className="form-group">
                                <label>Pharmacy Name</label>
                                <div className="input-wrapper">
                                    <Building size={18} className="input-icon" />
                                    <input type="text" placeholder="HealthPlus Pharmacy" required />
                                </div>
                            </div>
                        )}
                        {role === 'pharmacy' && (
                            <div className="form-group">
                                <label>Pharmacy Matricule</label>
                                <div className="input-wrapper">
                                    <FileBadge size={18} className="input-icon" />
                                    <input type="text" placeholder="matricule" required />
                                </div>
                            </div>
                        )}
                        {role === 'pharmacy' && (
                            <div className="form-group">
                                <label>Adress</label>
                                <div className="input-wrapper">
                                    <Building size={18} className="input-icon" />
                                    <input type="text" placeholder="123 Main Street, City" required />
                                </div>
                            </div>
                        )}
                        {role === 'patient' && (
                            <div className="form-group">
                                <label>CIN</label>
                                <div className="input-wrapper">
                                    <Building size={18} className="input-icon" />
                                    <input type="text" placeholder="123 Main Street, City" required />
                                </div>
                            </div>
                        )}

                        <div className="form-group">
                            <label>Email Address</label>
                            <div className="input-wrapper">
                                <Mail size={18} className="input-icon" />
                                <input type="email" placeholder="name@example.com" required />
                            </div>
                        </div>
                        <div className="form-group">
                            <label>Phone</label>
                            <div className="input-wrapper">
                                <Mail size={18} className="input-icon" />
                                <input type="email" placeholder="xx-xxx-xxx" required />
                            </div>
                        </div>

                        <div className="form-group">
                            <label>Password</label>
                            <div className="input-wrapper">
                                <Lock size={18} className="input-icon" />
                                <input type="password" placeholder="Create a password" required />
                            </div>
                        </div>

                        <button type="submit" className="btn-submit">
                            Create Account <ArrowRight size={18} />
                        </button>
                    </form>

                    <div className="auth-footer">
                        <p>Already have an account? <Link to="/login">Sign in</Link></p>
                    </div>
                </motion.div>
            </div>
            <Footer />
        </div>
    );
};

export default SignupPage;
