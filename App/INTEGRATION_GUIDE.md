# Frontend-Backend Integration Guide

## Overview
This document outlines the complete integration between the frontend (React) and backend (Spring Boot) of the PharmaSeek application.

---

## 1. Architecture Overview

### Communication Flow
```
Frontend (React)
    ↓
API Client Service (HTTP Client)
    ↓
Backend (Spring Boot)
    ↓
Database (MySQL)
```

### Authentication Flow
```
1. User Login/Signup
2. Backend validates and returns JWT tokens
3. Frontend stores accessToken in localStorage
4. Frontend includes token in Authorization header for subsequent requests
5. JwtAuthenticationFilter on backend validates token for each request
```

---

## 2. Service Layer (Frontend)

All HTTP communication is handled through service files in `/src/services/`:

### 2.1 API Client (`apiClient.js`)
**Purpose**: Core HTTP client with centralized configuration

**Key Features**:
- Base URL configuration: `http://localhost:8088/api`
- JWT token management (get/set)
- Automatic authorization header injection
- Error handling and 401 (unauthorized) response handling
- Supports: GET, POST, PUT, DELETE, PATCH methods

**Usage**:
```javascript
import apiClient from '../services/apiClient';

// GET request
const data = await apiClient.get('/endpoint');

// POST request
const data = await apiClient.post('/endpoint', {body});

// Automatic token inclusion
// Authorization: Bearer {accessToken}
```

### 2.2 Authentication Service (`authService.js`)
**Purpose**: Handle login, signup, token refresh, and password reset

**Methods**:
- `registerClient(clientData)` - Register patient account
- `registerPharmacy(pharmacyData)` - Register pharmacy account
- `login(email, password)` - Authenticate user
- `getCurrentUser()` - Get authenticated user info
- `refreshToken()` - Refresh access token
- `logout()` - Clear authentication
- `forgotPassword(email)` - Request password reset
- `resetPassword(token, newPassword)` - Complete password reset
- `isAuthenticated()` - Check if user is logged in

### 2.3 Drug Service (`drugService.js`)
**Purpose**: Handle drug/medication operations

**Methods**:
- `getAllDrugs()` - Get all available drugs
- `getDrugById(drugId)` - Get specific drug
- `searchDrugs(searchQuery)` - Search by name
- `createDrug(drugData)` - Create new drug (Pharmacy/Admin)
- `updateDrug(drugId, drugData)` - Update drug info
- `deleteDrug(drugId)` - Delete drug (Admin)

### 2.4 Pharmacy Stock Service (`pharmacyStockService.js`)
**Purpose**: Handle inventory/stock management

**Methods**:
- `getAllStock()` - Get all stock (Admin)
- `getStockById(stockId)` - Get stock item
- `getPharmacyInventory(pharmacyId)` - Get pharmacy's inventory
- `getStockByDrug(drugId)` - Find stock by drug
- `addStock(stockData)` - Add new stock item
- `updateStock(stockId, stockData)` - Update stock quantity/price
- `deleteStock(stockId)` - Remove stock item

### 2.5 Reservation Service (`reservationService.js`)
**Purpose**: Handle reservation operations

**Methods**:
- `getAllReservations()` - Get all reservations (Admin)
- `getReservationById(reservationId)` - Get specific reservation
- `getMyReservations()` - Get authenticated user's reservations
- `getPharmacyReservations(pharmacyId)` - Get pharmacy's reservations
- `createReservation(reservationData)` - Create new reservation
- `updateReservation(reservationId, data)` - Update reservation
- `updateReservationStatus(reservationId, status)` - Change status
- `cancelReservation(reservationId)` - Cancel reservation

### 2.6 User Service (`userService.js`)
**Purpose**: Handle user management operations

**Methods**:
- `getAllUsers()` - Get all users (Admin)
- `getUserById(userId)` - Get user info
- `getUsersByRole(role)` - Get users by role (Admin)
- `updateUserRole(userId, role)` - Change user role (Admin)

---

## 3. State Management

### Authentication Context (`AuthContext.jsx`)
**Purpose**: Provide authentication state globally

**Context Values**:
```javascript
{
  user: {             // Current authenticated user
    id, name, email, phone, roles, ...
  },
  isLoading: boolean, // Loading state
  isAuthenticated: boolean,
  error: string,      // Error message
  
  // Methods
  login(email, password),
  signup(userData, userType),
  logout(),
  forgotPassword(email),
  resetPassword(token, newPassword)
}
```

**Usage**:
```javascript
import { useAuth } from '../context/AuthContext';

function MyComponent() {
  const { user, isAuthenticated, login, logout } = useAuth();
  
  if (!isAuthenticated) return <Redirect to="/login" />;
  
  return <div>Welcome, {user.name}</div>;
}
```

---

## 4. Protected Routes

### ProtectedRoute Component (`ProtectedRoute.jsx`)
**Purpose**: Restrict access to authenticated users only

**Props**:
- `children` - Component to render
- `requiredRole` - Optional role check (CLIENT, PHARMACY, ADMIN)

**Usage**:
```javascript
<Route
  path="/client"
  element={
    <ProtectedRoute requiredRole="CLIENT">
      <ClientDashboard />
    </ProtectedRoute>
  }
/>
```

**Behavior**:
- Shows loading screen while checking authentication
- Redirects to `/login` if not authenticated
- Redirects to `/` if user lacks required role
- Otherwise renders protected component

---

## 5. Updated Pages

### 5.1 Login Page (`LoginPage.jsx`)
**Integration**:
- Uses `useAuth` hook for login method
- Calls `authService.login(email, password)`
- Stores JWT token via `apiClient.setAccessToken()`
- Redirects based on user role (CLIENT → /client, PHARMACY → /pharmacy)
- Shows loading state during submission
- Displays error messages via popup toast

**Important**: All form fields are now controlled inputs with proper validation

### 5.2 Signup Page (`SignupPage.jsx`)
**Integration**:
- Uses `useAuth` hook for signup method
- Supports two registration flows:
  - **Client**: name, email, phone, password
  - **Pharmacy**: pharmacyName, email, taxId, address, password, (optional) lat/long
- Validates field requirements and password length (min 8)
- Phone validation (8 digits for patient)
- Redirects to login on success
- Shows error messages via popup toast

**Important**: 
- Tax ID field is labeled as "Tax ID / Registration Number"
- Pharmacy can optionally provide coordinates
- All inputs properly disabled during submission

---

## 6. Implementation Guide for Remaining Pages

### For ClientDashboard and PharmacyDashboard Integration

The following template should be used:

```javascript
import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import reservationService from '../services/reservationService';
import drugService from '../services/drugService';

function ClientDashboard() {
  const { user } = useAuth();
  const [reservations, setReservations] = useState([]);
  const [drugs, setDrugs] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  // Load data on component mount
  useEffect(() => {
    const loadData = async () => {
      try {
        setIsLoading(true);
        
        // Fetch user's reservations
        const resData = await reservationService.getMyReservations();
        setReservations(resData);
        
        // Fetch all drugs for search
        const drugData = await drugService.getAllDrugs();
        setDrugs(drugData);
        
      } catch (err) {
        setError(err.message);
        popup.error('Failed to load data');
      } finally {
        setIsLoading(false);
      }
    };

    loadData();
  }, []);

  // Create reservation handler
  const handleCreateReservation = async (items, pharmacyId) => {
    try {
      const reservation = await reservationService.createReservation({
        clientId: user.id,
        pharmacyId,
        items
      });
      
      popup.valid('Reservation created successfully');
      // Refresh list
      const updated = await reservationService.getMyReservations();
      setReservations(updated);
      
    } catch (err) {
      popup.error(err.message);
    }
  };

  if (isLoading) return <LoadingScreen />;
  if (error) return <ErrorScreen error={error} />;

  return (
    // Component JSX using real data
  );
}
```

---

## 7. Error Handling

### Error Types and Handling

```javascript
try {
  await someApiCall();
} catch (error) {
  // Check error status
  if (error.status === 401) {
    // Unauthorized - redirect to login (auto-handled by apiClient)
  } else if (error.status === 403) {
    popup.error('You do not have permission for this action');
  } else if (error.status === 404) {
    popup.error('Resource not found');
  } else if (error.status === 400) {
    popup.error(error.data.message || 'Invalid request');
  } else if (error.status === 500) {
    popup.error('Server error. Please try again later');
  } else {
    popup.error(error.message || 'An error occurred');
  }
}
```

---

## 8. Data Models

### User Response
```javascript
{
  id: number,
  name: string,
  email: string,
  phone: string,
  roles: string[], // ["CLIENT"] or ["PHARMACY"]
  createdAt: ISO8601 datetime
}
```

### Reservation Response
```javascript
{
  id: number,
  clientId: number,
  pharmacyId: number,
  status: "PENDING" | "CONFIRMED" | "CANCELED" | "COMPLETED" | "EXPIRED",
  totalPrice: decimal,
  reservedAt: ISO8601 datetime,
  expirationTime: ISO8601 datetime,
  notes: string,
  items: [
    {
      id: number,
      drugId: number,
      quantity: number,
      unitPrice: decimal,
      subtotal: decimal
    }
  ]
}
```

### Drug Response
```javascript
{
  id: number,
  name: string,
  description: string,
  barCode: string,
  category: string,
  manufacturer: string,
  requiresPrescription: boolean,
  createdAt: ISO8601 datetime
}
```

### PharmacyStock Response
```javascript
{
  id: number,
  pharmacyId: number,
  drugId: number,
  quantity: number,
  price: decimal,
  reservationDelayMinutes: number,
  createdAt: ISO8601 datetime
}
```

---

## 9. Environment Configuration

### Frontend Environment Variables

Create `.env` file in `/frontend/` directory:

```
VITE_API_BASE_URL=http://localhost:8088/api
```

Or use default: `http://localhost:8088/api`

### Backend Configuration

Already configured in `application.yaml`:
```yaml
server:
  port: 8088
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/pharmacy-app
    username: root
    password: root
  jpa:
    show-sql: true
```

---

## 10. Running the Application

### Start Backend
```bash
cd /home/toji/Desktop/desk/PFA-files/App/backend
mvn spring-boot:run
# Runs on http://localhost:8088
```

### Start Frontend
```bash
cd /home/toji/Desktop/desk/PFA-files/App/frontend
npm install
npm run dev
# Runs on http://localhost:5173
```

### Start Database
```bash
# MySQL should be running on localhost:3306
# Database: pharmacy-app
# User: root
# Password: root
```

---

## 11. Testing the Integration

### Test Login Flow
1. Navigate to http://localhost:5173/login
2. Enter credentials from database
3. Click "Sign In"
4. Should redirect to appropriate dashboard
5. Token should be visible in localStorage as `accessToken`

### Test API Calls
```javascript
// In browser console
const token = localStorage.getItem('accessToken');
console.log('Token:', token);

// Verify it's being sent
// Check Network tab in DevTools → Headers → Authorization
```

### Test Protected Routes
1. Try accessing `/client` without logging in → Should redirect to `/login`
2. Login as CLIENT → Access `/client` → Should work
3. Try accessing `/pharmacy` as CLIENT → Should redirect to `/`
4. Login as PHARMACY → Access `/pharmacy` → Should work

---

## 12. Debugging Tips

### Common Issues and Solutions

**Issue**: 401 Unauthorized on every request
- **Solution**: Check token is stored correctly in localStorage
- Check token hasn't expired (2-hour expiry)
- Verify `Authorization` header is being sent in Network tab

**Issue**: CORS errors
- **Solution**: Ensure backend allows `http://localhost:5173` in CORS config
- Check `SecurityConfig.java` allows credentials

**Issue**: Login succeeds but page doesn't redirect
- **Solution**: Check user roles in response match route requirements
- Verify `ProtectedRoute` is checking roles correctly

**Issue**: API calls return 404
- **Solution**: Verify backend is running on port 8088
- Check endpoint path matches backend routes
- Verify database migrations ran successfully

---

## 13. Next Steps

Remaining work for complete integration:

1. **ClientDashboard**:
   - Fetch reservations from `reservationService.getMyReservations()`
   - Search drugs via `drugService.searchDrugs(query)`
   - Create reservations via `reservationService.createReservation()`
   - Update reservation status

2. **PharmacyDashboard**:
   - Fetch pharmacy's inventory via `pharmacyStockService.getPharmacyInventory()`
   - Fetch pharmacy's reservations via `reservationService.getPharmacyReservations()`
   - Manage stock (add/edit/delete)
   - Update reservation statuses

3. **Additional Features**:
   - Implement password reset flow
   - Add prescription image upload and processing
   - Integrate location/maps for pharmacy search
   - Real-time notifications

---

## 14. File Structure Reference

```
/frontend/src/
├── services/
│   ├── apiClient.js              # Core HTTP client
│   ├── authService.js            # Authentication
│   ├── drugService.js            # Drug operations
│   ├── pharmacyStockService.js   # Inventory
│   ├── reservationService.js     # Reservations
│   └── userService.js            # User management
├── context/
│   └── AuthContext.jsx           # Authentication state
├── components/
│   ├── ProtectedRoute.jsx        # Route protection
│   └── ... (other components)
├── pages/
│   ├── LoginPage.jsx             # ✅ Integrated
│   ├── SignupPage.jsx            # ✅ Integrated
│   ├── ClientDashboard.jsx       # ⏳ TODO
│   ├── PharmacyDashboard.jsx     # ⏳ TODO
│   └── ... (other pages)
└── App.jsx                        # ✅ Integrated with AuthProvider
```

---

## 15. Important Notes

⚠️ **DO NOT MODIFY BACKEND** - All changes should be frontend-only

✅ **Already Completed**:
- API client service with JWT handling
- Authentication Context for state management
- Protected Route component
- Login and Signup page integration
- Error handling and loading states

📝 **Remaining Work**:
- ClientDashboard integration
- PharmacyDashboard integration
- Real API data usage instead of mock data
- Additional features as needed

---

**Last Updated**: April 20, 2026
**Status**: Integration in Progress ✅
