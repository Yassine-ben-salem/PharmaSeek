import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import {
    LayoutDashboard,
    Package,
    CalendarCheck,
    Settings,
    LogOut,
    Plus,
    Search,
    Filter,
    Edit2,
    Trash2,
    CheckCircle2,
    Clock,
    TrendingUp,
    AlertTriangle,
    ArrowLeft
} from 'lucide-react';
import './PharmacyDashboard.css';

const PharmacyDashboard = () => {
    const [activeTab, setActiveTab] = useState('overview');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [currentProductId, setCurrentProductId] = useState(null);
    const [newProduct, setNewProduct] = useState({
        name: '',
        category: '',
        stock: '',
        price: '',
        status: 'In Stock'
    });

    // Dummy Inventory Data
    const [inventoryData, setInventoryData] = useState([
        { id: 1, name: 'Amoxicillin 500mg', category: 'Antibiotic', stock: 450, price: '€12.50', status: 'In Stock' },
        { id: 2, name: 'Paracetamol 500mg', category: 'Analgesic', stock: 25, price: '€4.20', status: 'Low Stock' },
        { id: 3, name: 'Ibuprofen 400mg', category: 'Anti-inflammatory', stock: 120, price: '€6.80', status: 'In Stock' },
        { id: 4, name: 'Cetirizine 10mg', category: 'Antihistamine', stock: 0, price: '€9.00', status: 'Out of Stock' },
    ]);

    // Dummy Reservations Data
    const reservationsData = [
        { id: 'RES-001', patient: 'Jean Dupont', medicine: 'Amoxicillin 500mg', date: '2024-02-16', status: 'Ready' },
        { id: 'RES-002', patient: 'Marie Curie', medicine: 'Ibuprofen 400mg', date: '2024-02-16', status: 'Pending' },
        { id: 'RES-003', patient: 'Pierre Martin', medicine: 'Paracetamol 500mg', date: '2024-02-15', status: 'Picked Up' },
    ];

    const renderOverview = () => (
        <>
            <div className="stats-grid">
                <div className="stat-card">
                    <div className="stat-icon" style={{ background: 'rgba(56, 189, 248, 0.1)', color: 'var(--pharmacy-accent)' }}>
                        <Package size={24} />
                    </div>
                    <div className="stat-info">
                        <div className="stat-value">1,284</div>
                        <div className="stat-label">Total Products</div>
                    </div>
                </div>
                <div className="stat-card">
                    <div className="stat-icon" style={{ background: 'rgba(239, 68, 68, 0.2)', color: '#f87171' }}>
                        <AlertTriangle size={24} />
                    </div>
                    <div className="stat-info">
                        <div className="stat-value">12</div>
                        <div className="stat-label">Low Stock items</div>
                    </div>
                </div>
                <div className="stat-card">
                    <div className="stat-icon" style={{ background: 'rgba(16, 185, 129, 0.2)', color: '#34d399' }}>
                        <CalendarCheck size={24} />
                    </div>
                    <div className="stat-info">
                        <div className="stat-value">48</div>
                        <div className="stat-label">Reservations Today</div>
                    </div>
                </div>
                <div className="stat-card">
                    <div className="stat-icon" style={{ background: 'rgba(139, 92, 246, 0.2)', color: '#a78bfa' }}>
                        <TrendingUp size={24} />
                    </div>
                    <div className="stat-info">
                        <div className="stat-value">€2,450</div>
                        <div className="stat-label">Total Gained Today</div>
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
                                <th>Order ID</th>
                                <th>Patient</th>
                                <th>Medicine</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {reservationsData.slice(0, 3).map(res => (
                                <tr key={res.id}>
                                    <td><strong>{res.id}</strong></td>
                                    <td>{res.patient}</td>
                                    <td>{res.medicine}</td>
                                    <td>
                                        <span className={`badge ${res.status === 'Ready' ? 'badge-success' :
                                            res.status === 'Pending' ? 'badge-warning' : 'badge-info'
                                            }`}>
                                            {res.status}
                                        </span>
                                    </td>
                                    <td>
                                        <button className="btn-icon"><CheckCircle2 size={18} /></button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </>
    );

    const handleSaveProduct = (e) => {
        e.preventDefault();
        if (isEditing) {
            setInventoryData(inventoryData.map(item =>
                item.id === currentProductId ? { ...newProduct, id: currentProductId } : item
            ));
        } else {
            const id = inventoryData.length + 1;
            setInventoryData([...inventoryData, { ...newProduct, id }]);
        }
        setIsModalOpen(false);
        setIsEditing(false);
        setNewProduct({ name: '', category: '', stock: '', price: '', status: 'In Stock' });
    };

    const handleEditClick = (product) => {
        setIsEditing(true);
        setCurrentProductId(product.id);
        setNewProduct({
            name: product.name,
            category: product.category,
            stock: product.stock,
            price: product.price,
            status: product.status
        });
        setIsModalOpen(true);
    };

    const openAddModal = () => {
        setIsEditing(false);
        setNewProduct({ name: '', category: '', stock: '', price: '', status: 'In Stock' });
        setIsModalOpen(true);
    };

    const renderInventory = () => (
        <div className="section-container">
            <div className="section-header">
                <h2>Product Inventory</h2>
                <div className="action-btns">
                    <button className="btn btn-secondary"><Filter size={18} /> Filter</button>
                    <button className="btn btn-primary" onClick={openAddModal}><Plus size={18} /> Add New Product</button>
                </div>
            </div>
            <div className="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>Medicine Name</th>
                            <th>Category</th>
                            <th>Stock</th>
                            <th>Price</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {inventoryData.map(item => (
                            <tr key={item.id}>
                                <td><strong>{item.name}</strong></td>
                                <td>{item.category}</td>
                                <td>{item.stock}</td>
                                <td>{item.price}</td>
                                <td>
                                    <span className={`badge ${item.status === 'In Stock' ? 'badge-success' :
                                        item.status === 'Low Stock' ? 'badge-warning' : 'badge-danger'
                                        }`}>
                                        {item.status}
                                    </span>
                                </td>
                                <td>
                                    <div className="action-btns">
                                        <button className="btn-icon" onClick={() => handleEditClick(item)}><Edit2 size={16} /></button>
                                        <button className="btn-icon"><Trash2 size={16} /></button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );

    const renderReservations = () => (
        <div className="section-container">
            <div className="section-header">
                <h2>Manage Reservations</h2>
                <div className="search-bar" style={{ position: 'relative', width: '300px' }}>
                    <Search size={18} style={{ position: 'absolute', left: '12px', top: '50%', transform: 'translateY(-50%)', color: 'rgba(255, 255, 255, 0.5)' }} />
                    <input type="text" placeholder="Search Patient or Order ID..." style={{ width: '100%', padding: '0.6rem 1rem 0.6rem 2.5rem', borderRadius: '10px', border: '1px solid rgba(255, 255, 255, 0.1)', background: 'rgba(255, 255, 255, 0.05)', color: 'white' }} />
                </div>
            </div>
            <div className="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Patient Name</th>
                            <th>Medicine</th>
                            <th>Date</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {reservationsData.map(res => (
                            <tr key={res.id}>
                                <td><strong>{res.id}</strong></td>
                                <td>{res.patient}</td>
                                <td>{res.medicine}</td>
                                <td>{res.date}</td>
                                <td>
                                    <span className={`badge ${res.status === 'Ready' ? 'badge-success' :
                                        res.status === 'Pending' ? 'badge-warning' : 'badge-info'
                                        }`}>
                                        {res.status}
                                    </span>
                                </td>
                                <td>
                                    <div className="action-btns">
                                        <button className="btn-icon" title="Mark as Ready"><CheckCircle2 size={16} /></button>
                                        <button className="btn-icon" title="View Details"><Clock size={16} /></button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );

    return (
        <div className="dashboard-container">
            {/* Sidebar */}
            <aside className="dashboard-sidebar">
                <div className="sidebar-brand">
                    <div style={{ padding: '8px', background: 'var(--pharmacy-accent)', borderRadius: '10px', color: 'var(--pharmacy-blue)' }}>
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
                        <span>Dashboard</span>
                    </button>
                    <button
                        className={`nav-item ${activeTab === 'inventory' ? 'active' : ''}`}
                        onClick={() => setActiveTab('inventory')}
                    >
                        <Package size={20} />
                        <span>Inventory</span>
                    </button>
                    <button
                        className={`nav-item ${activeTab === 'reservations' ? 'active' : ''}`}
                        onClick={() => setActiveTab('reservations')}
                    >
                        <CalendarCheck size={20} />
                        <span>Reservations</span>
                    </button>
                    <button className="nav-item">
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
                            {activeTab === 'overview' ? 'Welcome back, Pharmacy!' :
                                activeTab === 'inventory' ? 'Inventory Management' : 'Reservations Overview'}
                        </h1>
                        <p>
                            {activeTab === 'overview' ? 'Here is what is happening today.' :
                                activeTab === 'inventory' ? 'Manage and track your medical stock.' : 'Review and process patient reservations.'}
                        </p>
                    </div>
                    <div className="header-actions">
                        <button className="btn btn-secondary" style={{ marginRight: '1rem' }}>Today's Brief</button>
                        <Link to="/" className="btn btn-outline">
                            <ArrowLeft size={16} style={{ marginRight: '8px' }} /> Landing Page
                        </Link>
                    </div>
                </header>

                <div className="dashboard-content">
                    {activeTab === 'overview' && renderOverview()}
                    {activeTab === 'inventory' && renderInventory()}
                    {activeTab === 'reservations' && renderReservations()}
                </div>

                {isModalOpen && (
                    <div className="modal-overlay">
                        <div className="modal-content">
                            <h2>{isEditing ? 'Edit Product' : 'Add New Product'}</h2>
                            <form onSubmit={handleSaveProduct}>
                                <div className="form-group">
                                    <label>Medicine Name</label>
                                    <input
                                        type="text"
                                        required
                                        value={newProduct.name}
                                        onChange={(e) => setNewProduct({ ...newProduct, name: e.target.value })}
                                        placeholder="e.g. Lisinopril 10mg"
                                    />
                                </div>
                                <div className="form-group">
                                    <label>Category</label>
                                    <input
                                        type="text"
                                        required
                                        value={newProduct.category}
                                        onChange={(e) => setNewProduct({ ...newProduct, category: e.target.value })}
                                        placeholder="e.g. Hypertension"
                                    />
                                </div>
                                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                                    <div className="form-group">
                                        <label>Stock Level</label>
                                        <input
                                            type="number"
                                            required
                                            value={newProduct.stock}
                                            onChange={(e) => setNewProduct({ ...newProduct, stock: e.target.value })}
                                            placeholder="Quantity"
                                        />
                                    </div>
                                    <div className="form-group">
                                        <label>Price</label>
                                        <input
                                            type="text"
                                            required
                                            value={newProduct.price}
                                            onChange={(e) => setNewProduct({ ...newProduct, price: e.target.value })}
                                            placeholder="€0.00"
                                        />
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label>Initial Status</label>
                                    <select
                                        value={newProduct.status}
                                        onChange={(e) => setNewProduct({ ...newProduct, status: e.target.value })}
                                    >
                                        <option value="In Stock">In Stock</option>
                                        <option value="Low Stock">Low Stock</option>
                                        <option value="Out of Stock">Out of Stock</option>
                                    </select>
                                </div>
                                <div className="form-actions">
                                    <button type="button" className="btn btn-outline" onClick={() => setIsModalOpen(false)}>Cancel</button>
                                    <button type="submit" className="btn btn-primary">
                                        {isEditing ? 'Update Product' : 'Save Product'}
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                )}
            </main>
        </div>
    );
};

export default PharmacyDashboard;
