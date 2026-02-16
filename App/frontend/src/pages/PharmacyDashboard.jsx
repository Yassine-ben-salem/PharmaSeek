import React from 'react';
import { Link } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';

const PharmacyDashboard = () => {
    return (
        <div className="container" style={{ padding: '2rem' }}>
            <Link to="/" className="btn btn-primary" style={{ marginBottom: '2rem' }}>
                <ArrowLeft size={20} /> Back to Home
            </Link>
            <h1>Pharmacy Portal</h1>
            <p>Welcome to the pharmacy interface. Manage your inventory and orders here.</p>
        </div>
    );
};

export default PharmacyDashboard;
