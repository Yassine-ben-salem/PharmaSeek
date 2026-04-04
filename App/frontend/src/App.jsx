import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LandingPage from './pages/LandingPage';
import ClientDashboard from './pages/ClientDashboard';
import PharmacyDashboard from './pages/PharmacyDashboard';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage';
import './App.css';

function App() {
  return (
    <Router>
      <div className="app-container">
        <Routes>
          <Route path="/" element={<LandingPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignupPage />} />
          <Route path="/client" element={<ClientDashboard />} />
          <Route path="/pharmacy" element={<PharmacyDashboard />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
