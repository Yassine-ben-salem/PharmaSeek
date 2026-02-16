import React from 'react';
import { Link } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';

const ClientDashboard = () => {
    return (
        <div className="container" style={{ padding: '2rem' }}>
            <Link to="/" className="btn btn-secondary" style={{ marginBottom: '2rem' }}>
                <ArrowLeft size={20} /> Back to Home
            </Link>
            <h1>Client Portal</h1>
            <p>Welcome to the client interface. Search for medicines and pharmacies here.</p>
        </div>
    );
};

export default ClientDashboard;
