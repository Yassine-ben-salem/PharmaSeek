import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import {
    LayoutDashboard,
    Search,
    CalendarCheck,
    Settings,
    LogOut,
    MapPin,
    ArrowLeft,
    Package,
    Navigation,
    Clock,
    CheckCircle2,
    X,
    Sun,
    Moon
} from 'lucide-react';
import './ClientDashboard.css';
import pharmaciesData from '../data/pharmacies.json';
import medicinesData from '../data/medicines.json';
import initialReservations from '../data/reservations.json';

const ClientDashboard = () => {
    const [activeTab, setActiveTab] = useState('overview');
    const [searchQuery, setSearchQuery] = useState('');
    const [selectedPharmacy, setSelectedPharmacy] = useState(null);
    const [isMapModalOpen, setIsMapModalOpen] = useState(false);
    const [isReserveModalOpen, setIsReserveModalOpen] = useState(false);
    const [currentMedicine, setCurrentMedicine] = useState(null);
    const [theme, setTheme] = useState(localStorage.getItem('theme') || 'light');

    React.useEffect(() => {
        document.documentElement.setAttribute('data-theme', theme);
        localStorage.setItem('theme', theme);
    }, [theme]);

    const toggleTheme = () => {
        setTheme(prevTheme => prevTheme === 'light' ? 'dark' : 'light');
    };

    // Mock Data from JSON
    const [reservations, setReservations] = useState(initialReservations);

    // Medicines and Pharmacies are now imported from JSON

    const filteredPharmacies = pharmaciesData.filter(pharma =>
        searchQuery && pharma.stock.some(med => med.toLowerCase().includes(searchQuery.toLowerCase()))
    );

    const handleReserve = (pharmacy, medicineName) => {
        const newRes = {
            id: `RES-CK-00${reservations.length + 1}`,
            pharmacy: pharmacy.name,
            medicine: medicineName,
            date: new Date().toISOString().split('T')[0],
            status: 'Pending'
        };
        setReservations([newRes, ...reservations]);
        setIsReserveModalOpen(false);
        setActiveTab('reservations');
    };

    const openMap = (pharmacy) => {
        setSelectedPharmacy(pharmacy);
        setIsMapModalOpen(true);
    };

    const renderOverview = () => (
        <>
            <div className="stats-grid">
                <div className="stat-card">
                    <div className="stat-icon" style={{ background: 'rgba(16, 185, 129, 0.1)', color: 'var(--accent-secondary)' }}>
                        <CalendarCheck size={24} />
                    </div>
                    <div className="stat-info">
                        <div className="stat-value">{reservations.length}</div>
                        <div className="stat-label">Active Reservations</div>
                    </div>
                </div>
                <div className="stat-card">
                    <div className="stat-icon" style={{ background: 'rgba(16, 185, 129, 0.1)', color: '#10b981' }}>
                        <CheckCircle2 size={24} />
                    </div>
                    <div className="stat-info">
                        <div className="stat-value">5</div>
                        <div className="stat-label">Completed Orders</div>
                    </div>
                </div>
                <div className="stat-card">
                    <div className="stat-icon" style={{ background: 'rgba(139, 92, 246, 0.1)', color: '#8b5cf6' }}>
                        <MapPin size={24} />
                    </div>
                    <div className="stat-info">
                        <div className="stat-value">12</div>
                        <div className="stat-label">Nearby Pharmacies</div>
                    </div>
                </div>
            </div>

            <div className="section-container">
                <div className="section-header">
                    <h2>Recent Reservations</h2>
                    <button className="btn btn-primary" onClick={() => setActiveTab('reservations')}>View All</button>
                </div>
                <div className="table-wrapper">
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Pharmacy</th>
                                <th>Medicine</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {reservations.slice(0, 3).map(res => (
                                <tr key={res.id}>
                                    <td><strong>{res.id}</strong></td>
                                    <td>{res.pharmacy}</td>
                                    <td>{res.medicine}</td>
                                    <td>
                                        <span className={`badge ${res.status === 'Ready' ? 'badge-success' : 'badge-warning'}`}>
                                            {res.status}
                                        </span>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </>
    );

    const renderSearch = () => (
        <div className="section-container">
            <div className="section-header">
                <h2>Search Medicines</h2>
                <div className="search-bar" style={{ position: 'relative', width: '400px' }}>
                    <Search size={18} style={{ position: 'absolute', left: '12px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-secondary)' }} />
                    <input
                        type="text"
                        placeholder="Type medicine name (e.g. Amoxicillin)..."
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                        style={{ width: '100%', padding: '0.8rem 1rem 0.8rem 2.5rem', borderRadius: '15px', border: '1px solid var(--border-color)', background: 'var(--bg-secondary)', color: 'var(--text-primary)' }}
                    />
                </div>
            </div>

            {searchQuery ? (
                <div className="search-results">
                    <h3>Pharmacies with "{searchQuery}" in stock:</h3>
                    <div className="search-results-grid">
                        {filteredPharmacies.length > 0 ? (
                            filteredPharmacies.map(pharma => (
                                <div key={pharma.id} className="pharmacy-card">
                                    <div className="pharmacy-info">
                                        <h3>{pharma.name}</h3>
                                        <p><MapPin size={14} /> {pharma.address}</p>
                                        <p><Navigation size={14} /> {pharma.distance} away</p>
                                    </div>
                                    <div className="stock-status">
                                        <span className="badge badge-success">In Stock</span>
                                        <div className="action-btns" style={{ marginTop: '1rem', display: 'flex', gap: '0.5rem' }}>
                                            <button
                                                className="btn btn-secondary"
                                                style={{ padding: '0.5rem 1rem', fontSize: '0.75rem', flex: 1 }}
                                                onClick={() => openMap(pharma)}
                                            >
                                                <MapPin size={14} style={{ marginRight: '4px' }} /> See Location
                                            </button>
                                            <button
                                                className="btn btn-primary"
                                                style={{ padding: '0.5rem 1rem', fontSize: '0.75rem', flex: 1 }}
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    setCurrentMedicine(searchQuery);
                                                    setSelectedPharmacy(pharma);
                                                    setIsReserveModalOpen(true);
                                                }}
                                            >
                                                Reserve Now
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            ))
                        ) : (
                            <p style={{ color: 'var(--text-secondary)', marginTop: '1rem' }}>No pharmacies found with this medicine in stock.</p>
                        )}
                    </div>
                </div>
            ) : (
                <div style={{ textAlign: 'center', padding: '3rem', color: 'var(--text-secondary)' }}>
                    <Package size={48} style={{ marginBottom: '1rem', opacity: 0.5 }} />
                    <p>Enter a medicine name to see available pharmacies nearby.</p>
                </div>
            )}
        </div>
    );

    const renderReservations = () => (
        <div className="section-container">
            <div className="section-header">
                <h2>My Reservations</h2>
            </div>
            <div className="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Pharmacy</th>
                            <th>Medicine</th>
                            <th>Date</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        {reservations.map(res => (
                            <tr key={res.id}>
                                <td><strong>{res.id}</strong></td>
                                <td>{res.pharmacy}</td>
                                <td>{res.medicine}</td>
                                <td>{res.date}</td>
                                <td>
                                    <span className={`badge ${res.status === 'Ready' ? 'badge-success' : 'badge-warning'}`}>
                                        {res.status}
                                    </span>
                                </td>
                                <td>
                                    {res.status === 'Ready' ? (
                                        <span style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>Pick it up!</span>
                                    ) : (
                                        <button className="btn-icon" title="Cancel Reservation"><X size={16} /></button>
                                    )}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );

    const renderSettings = () => (
        <div className="section-container">
            <div className="section-header">
                <h2>Account Settings</h2>
            </div>
            <form style={{ maxWidth: '600px' }}>
                <div className="form-group">
                    <label>Full Name</label>
                    <input type="text" defaultValue="John Doe" />
                </div>
                <div className="form-group">
                    <label>Email Address</label>
                    <input type="email" defaultValue="john.doe@example.com" />
                </div>
                <div className="form-group">
                    <label>Phone Number</label>
                    <input type="tel" defaultValue="+33 6 12 34 56 78" />
                </div>
                <div className="form-group">
                    <label>Preferred Pharmacy</label>
                    <select>
                        <option>City Pharma</option>
                        <option>Green Cross</option>
                        <option>HealthFirst</option>
                    </select>
                </div>
                <button className="btn btn-primary" type="button" onClick={() => alert('Settings saved!')}>Save Changes</button>
            </form>
        </div>
    );

    return (
        <div className="dashboard-container">
            {/* Sidebar */}
            <aside className="dashboard-sidebar">
                <div className="sidebar-brand">
                    <div style={{ padding: '8px', background: 'white', borderRadius: '10px', color: 'var(--accent-secondary)' }}>
                        <Package size={24} />
                    </div>
                    <span>PharmaSeek</span>
                </div>

                <nav className="sidebar-nav">
                    <button
                        className={`nav-item ${activeTab === 'overview' ? 'active' : ''}`}
                        onClick={() => setActiveTab('overview')}
                    >
                        <LayoutDashboard size={20} />
                        <span>Overview</span>
                    </button>
                    <button
                        className={`nav-item ${activeTab === 'search' ? 'active' : ''}`}
                        onClick={() => setActiveTab('search')}
                    >
                        <Search size={20} />
                        <span>Find Medicines</span>
                    </button>
                    <button
                        className={`nav-item ${activeTab === 'reservations' ? 'active' : ''}`}
                        onClick={() => setActiveTab('reservations')}
                    >
                        <CalendarCheck size={20} />
                        <span>My Reservations</span>
                    </button>
                    <button
                        className={`nav-item ${activeTab === 'settings' ? 'active' : ''}`}
                        onClick={() => setActiveTab('settings')}
                    >
                        <Settings size={20} />
                        <span>Settings</span>
                    </button>
                </nav>

                <div className="sidebar-footer">
                    <Link to="/" className="nav-item">
                        <LogOut size={20} />
                        <span>Sign Out</span>
                    </Link>
                </div>
            </aside>

            {/* Main Content */}
            <main className="dashboard-main">
                <header className="dashboard-header">
                    <div className="header-title">
                        <h1>
                            {activeTab === 'overview' ? 'Hello, John!' :
                                activeTab === 'search' ? 'Find Your Medicine' :
                                    activeTab === 'reservations' ? 'Your Reservations' : 'Settings'}
                        </h1>
                        <p>
                            {activeTab === 'overview' ? 'Welcome back to your health portal.' :
                                activeTab === 'search' ? 'Search nearby pharmacies for what you need.' :
                                    activeTab === 'reservations' ? 'Track your active and past pickup orders.' : 'Manage your profile and preferences.'}
                        </p>
                    </div>
                    <div className="header-actions" style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
                        <button className="btn-icon" onClick={toggleTheme} title="Toggle Theme">
                            {theme === 'light' ? <Moon size={20} /> : <Sun size={20} />}
                        </button>
                        <Link to="/" className="btn btn-outline">
                            <ArrowLeft size={16} style={{ marginRight: '8px' }} /> Back to Home
                        </Link>
                    </div>
                </header>

                <div className="dashboard-content">
                    {activeTab === 'overview' && renderOverview()}
                    {activeTab === 'search' && renderSearch()}
                    {activeTab === 'reservations' && renderReservations()}
                    {activeTab === 'settings' && renderSettings()}
                </div>

                {/* Map Modal */}
                {isMapModalOpen && selectedPharmacy && (
                    <div className="modal-overlay" onClick={() => setIsMapModalOpen(false)}>
                        <div className="modal-content wide" onClick={e => e.stopPropagation()}>
                            <div className="section-header">
                                <h2>Location: {selectedPharmacy.name}</h2>
                                <button className="btn-icon" onClick={() => setIsMapModalOpen(false)}><X size={20} /></button>
                            </div>
                            <div className="map-placeholder">
                                <div className="map-grid"></div>
                                <div className="map-marker">
                                    <MapPin size={48} fill="currentColor" />
                                </div>
                                <div style={{ position: 'absolute', bottom: '20px', left: '20px', background: 'var(--dash-card)', padding: '1rem', borderRadius: '12px', boxShadow: '0 4px 12px rgba(0,0,0,0.1)', color: 'var(--dash-text)', border: '1px solid var(--dash-border)' }}>
                                    <p style={{ fontWeight: 600 }}>{selectedPharmacy.name}</p>
                                    <p style={{ fontSize: '0.8rem', opacity: 0.7 }}>{selectedPharmacy.address}</p>
                                </div>
                            </div>
                            <div className="form-actions">
                                <button className="btn btn-primary" onClick={() => setIsMapModalOpen(false)}>Close Map</button>
                            </div>
                        </div>
                    </div>
                )}

                {/* Reserve Modal */}
                {isReserveModalOpen && selectedPharmacy && (
                    <div className="modal-overlay" onClick={() => setIsReserveModalOpen(false)}>
                        <div className="modal-content" onClick={e => e.stopPropagation()}>
                            <h2>Confirm Reservation</h2>
                            <p>You are about to reserve <strong>{currentMedicine}</strong> at <strong>{selectedPharmacy.name}</strong>.</p>
                            <p style={{ margin: '1rem 0', padding: '1rem', background: 'var(--bg-secondary)', borderRadius: '12px', fontSize: '0.9rem' }}>
                                <Clock size={16} style={{ marginRight: '8px', verticalAlign: 'middle' }} />
                                The reservation will be valid for 24 hours.
                            </p>
                            <div className="form-actions">
                                <button className="btn btn-outline" onClick={() => setIsReserveModalOpen(false)}>Cancel</button>
                                <button className="btn btn-primary" onClick={() => handleReserve(selectedPharmacy, currentMedicine)}>Confirm Reservation</button>
                            </div>
                        </div>
                    </div>
                )}
            </main>
        </div>
    );
};

export default ClientDashboard;

