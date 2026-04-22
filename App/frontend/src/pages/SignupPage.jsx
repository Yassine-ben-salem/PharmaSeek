import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { Link, useNavigate } from 'react-router-dom';
import { Mail, Lock, ArrowRight, User, Stethoscope, Building, FileBadge, Phone, IdCard, Store, MapPin } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import usePopUp from '../components/usePopUp';
import './Auth.css';

const SignupPage = () => {
    const [role, setRole] = useState('client');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();
    const { signup } = useAuth();
    const {popup} = usePopUp();

    // Common fields
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');
    const [password, setPassword] = useState('');

    // Patient fields
    const [fullName, setFullName] = useState('');

    // Pharmacy fields
    const [pharmacyName, setPharmacyName] = useState('');
    const [taxId, setTaxId] = useState('');
    const [address, setAddress] = useState('');
    const [latitude, setLatitude] = useState('');
    const [longitude, setLongitude] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Validate fields
        if (role === 'client') {
            if (!fullName || !email || !phone || !password) {
                popup.error('Please fill in all fields');
                return;
            }
            if (password.length < 8) {
                popup.error('Password must be at least 8 characters');
                return;
            }
            if (phone.length !== 8) {
                popup.error('Phone number must be 8 digits');
                return;
            }
        } else {
            if (!pharmacyName || !email || !taxId || !address || !password) {
                popup.error('Please fill in all fields');
                return;
            }
            if (password.length < 8) {
                popup.error('Password must be at least 8 characters');
                return;
            }
        }

        setIsLoading(true);
        try {
            let userData;
            if (role === 'client') {
                userData = {
                    name: fullName,
                    email,
                    phone,
                    password,
                };
            } else {
                userData = {
                    pharmacyName,
                    email,
                    taxId,
                    password,
                    address,
                    latitude: latitude ? parseFloat(latitude) : 0,
                    longitude: longitude ? parseFloat(longitude) : 0,
                };
            }

            await signup(userData, role);
            popup.valid('Account created successfully! Please login.');
            
            // Redirect to login
            navigate('/login');
        } catch (error) {
            console.error('Signup error:', error);
            popup.error(error.message || 'Signup failed. Please try again.');
        } finally {
            setIsLoading(false);
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
                        <p>Join PharmaSeek today</p>
                    </div>

                    <div className="role-selector">
                        <button
                            className={`role-btn ${role === 'client' ? 'active' : ''}`}
                            onClick={() => setRole('client')}
                            disabled={isLoading}
                        >
                            <User size={18} />
                            Patient
                        </button>
                        <button
                            className={`role-btn ${role === 'pharmacy' ? 'active' : ''}`}
                            onClick={() => setRole('pharmacy')}
                            disabled={isLoading}
                        >
                            <Stethoscope size={18} />
                            Pharmacy
                        </button>
                    </div>
                    <form onSubmit={handleSubmit} className="auth-form">
                    {role === 'client' && (
                        <div className="form-group">
                            <label>Full Name</label>
                            <div className="input-wrapper">
                                <User size={18} className="input-icon" />
                                <input
                                    type="text"
                                    placeholder="John Doe"
                                    value={fullName}
                                    onChange={(e) => setFullName(e.target.value)}
                                    disabled={isLoading}
                                    required
                                />
                            </div>
                        </div>)}

                        {role === 'pharmacy' && (
                            <div className="form-group">
                                <label>Pharmacy Name</label>
                                <div className="input-wrapper">
                                    <Store size={18} className="input-icon" />
                                    <input
                                        type="text"
                                        placeholder="HealthPlus Pharmacy"
                                        value={pharmacyName}
                                        onChange={(e) => setPharmacyName(e.target.value)}
                                        disabled={isLoading}
                                        required
                                    />
                                </div>
                            </div>
                        )}
                        {role === 'pharmacy' && (
                            <div className="form-group">
                                <label>Tax ID / Registration Number</label>
                                <div className="input-wrapper">
                                    <FileBadge size={18} className="input-icon" />
                                    <input
                                        type="text"
                                        placeholder="Your tax ID"
                                        value={taxId}
                                        onChange={(e) => setTaxId(e.target.value)}
                                        disabled={isLoading}
                                        required
                                    />
                                </div>
                            </div>
                        )}
                        {role === 'pharmacy' && (
                            <div className="form-group">
                                <label>Address</label>
                                <div className="input-wrapper">
                                    <MapPin size={18} className="input-icon" />
                                    <input
                                        type="text"
                                        placeholder="123 Main Street, City"
                                        value={address}
                                        onChange={(e) => setAddress(e.target.value)}
                                        disabled={isLoading}
                                        required
                                    />
                                </div>
                            </div>
                        )}
                        {role === 'pharmacy' && (
                            <div className="form-row">
                                <div className="form-group">
                                    <label>Latitude (Optional)</label>
                                    <input
                                        type="number"
                                        step="0.0001"
                                        placeholder="0.0000"
                                        value={latitude}
                                        onChange={(e) => setLatitude(e.target.value)}
                                        disabled={isLoading}
                                    />
                                </div>
                                <div className="form-group">
                                    <label>Longitude (Optional)</label>
                                    <input
                                        type="number"
                                        step="0.0001"
                                        placeholder="0.0000"
                                        value={longitude}
                                        onChange={(e) => setLongitude(e.target.value)}
                                        disabled={isLoading}
                                    />
                                </div>
                            </div>
                        )}

                        <div className="form-group">
                            <label>Email Address</label>
                            <div className="input-wrapper">
                                <Mail size={18} className="input-icon" />
                                <input
                                    type="email"
                                    placeholder="name@example.com"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    disabled={isLoading}
                                    required
                                />
                            </div>
                        </div>
                        <div className="form-group">
                            <label>Phone (8 digits)</label>
                            <div className="input-wrapper">
                                <Phone size={18} className="input-icon" />
                                <input
                                    type="tel"
                                    placeholder="12345678"
                                    value={phone}
                                    onChange={(e) => setPhone(e.target.value)}
                                    disabled={isLoading}
                                    required
                                />
                            </div>
                        </div>

                        <div className="form-group">
                            <label>Password (min 8 characters)</label>
                            <div className="input-wrapper">
                                <Lock size={18} className="input-icon" />
                                <input
                                    type="password"
                                    placeholder="Create a password"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    disabled={isLoading}
                                    required
                                />
                            </div>
                        </div>

                        <button type="submit" className="btn-submit" disabled={isLoading}>
                            {isLoading ? 'Creating Account...' : 'Create Account'} <ArrowRight size={18} />
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
