# Quick Start Guide - Frontend-Backend Integration

## 🚀 Start Here

### Prerequisites
- Node.js and npm installed
- Java 17+ installed
- MySQL 8.x running on localhost:3306
- Database: `pharmacy-app` (user: root, password: root)

---

## 📋 Step-by-Step Setup

### 1️⃣ Start Backend (Terminal 1)
```bash
cd /home/toji/Desktop/desk/PFA-files/App/backend
mvn spring-boot:run
```
✅ Backend runs on `http://localhost:8088`

### 2️⃣ Start Frontend (Terminal 2)
```bash
cd /home/toji/Desktop/desk/PFA-files/App/frontend
npm install
npm run dev
```
✅ Frontend runs on `http://localhost:5173`

---

## 🧪 Test the Integration

### Test Path 1: Patient Registration & Login
1. Go to http://localhost:5173
2. Click **"Sign Up"**
3. Select **"Patient"** role
4. Fill form:
   - Full Name: `John Doe`
   - Email: `patient@test.com`
   - Phone: `12345678` (exactly 8 digits)
   - Password: `password123` (min 8 chars)
5. Click **"Create Account"**
6. Redirected to `/login`
7. Enter credentials and click **"Sign In"**
8. ✅ Redirected to **Client Dashboard** (`/client`)

### Test Path 2: Pharmacy Registration & Login
1. Go to http://localhost:5173
2. Click **"Sign Up"**
3. Select **"Pharmacy"** role
4. Fill form:
   - Pharmacy Name: `Health Pharmacy`
   - Email: `pharmacy@test.com`
   - Tax ID: `TX123456`
   - Address: `123 Main St`
   - Latitude: `48.8566` (optional)
   - Longitude: `2.3522` (optional)
   - Phone: `98765432`
   - Password: `password123`
5. Click **"Create Account"**
6. Redirected to `/login`
7. Enter credentials and click **"Sign In"**
8. ✅ Redirected to **Pharmacy Dashboard** (`/pharmacy`)

### Test Path 3: Protected Routes
1. Without logging in, try to access:
   - `http://localhost:5173/client` → ❌ Redirects to `/login`
   - `http://localhost:5173/pharmacy` → ❌ Redirects to `/login`
2. After login, verify:
   - Patient can access `/client` ✅
   - Patient cannot access `/pharmacy` → ❌ Redirects to `/`
   - Pharmacy can access `/pharmacy` ✅
   - Pharmacy cannot access `/client` → ❌ Redirects to `/`

---

## 🔍 Verify Integration

### Check 1: JWT Token
Open browser console and run:
```javascript
localStorage.getItem('accessToken')
// Should return a long JWT token starting with 'eyJ...'
```

### Check 2: API Requests
1. Open DevTools → Network tab
2. Do any action on the dashboard
3. Look for requests to `http://localhost:8088/api/...`
4. Check request headers include: `Authorization: Bearer {token}`

### Check 3: Database
```sql
SELECT * FROM user_account;
SELECT * FROM client;
SELECT * FROM pharmacy;
```
Should see your test accounts in the database.

---

## 💡 Usage Examples

### Use Authentication
```javascript
import { useAuth } from '../context/AuthContext';

function MyComponent() {
  const { user, isAuthenticated, logout } = useAuth();
  
  if (!isAuthenticated) {
    return <div>Please login</div>;
  }
  
  return <div>Hello, {user.name}!</div>;
}
```

### Make API Calls
```javascript
import reservationService from '../services/reservationService';
import drugService from '../services/drugService';

async function loadData() {
  try {
    // Get user's reservations
    const reservations = await reservationService.getMyReservations();
    
    // Get all drugs
    const drugs = await drugService.getAllDrugs();
    
    // Search drugs
    const results = await drugService.searchDrugs('amoxicillin');
    
  } catch (error) {
    console.error('API Error:', error.message);
  }
}
```

### Protect Routes
```javascript
import ProtectedRoute from '../components/ProtectedRoute';

<Route
  path="/client"
  element={
    <ProtectedRoute requiredRole="CLIENT">
      <ClientDashboard />
    </ProtectedRoute>
  }
/>
```

---

## 📂 File Reference

### New Service Files (Use These for API Calls)
```
src/services/
├── apiClient.js              ← Base HTTP client
├── authService.js            ← Login/Signup/Logout
├── drugService.js            ← Drug operations
├── pharmacyStockService.js   ← Inventory management
├── reservationService.js     ← Reservation operations
└── userService.js            ← User management
```

### New Context (Use in Components)
```
src/context/
└── AuthContext.jsx           ← useAuth() hook
```

### New Protected Routes
```
src/components/
└── ProtectedRoute.jsx        ← Wrapper for protected pages
```

---

## 🐛 Troubleshooting

### Issue: "Cannot GET /"
- Make sure frontend is running on port 5173
- Check: `npm run dev` output shows URL

### Issue: "Failed to fetch from backend"
- Make sure backend is running on port 8088
- Check: `mvn spring-boot:run` shows server started
- Verify MySQL is running and accessible

### Issue: Login fails with "Invalid credentials"
- Check backend logs for error details
- Verify user exists in database
- Try registering new test account first

### Issue: Token not saved in localStorage
- Check browser console for errors
- Verify no CSP violations
- Try clearing localStorage: `localStorage.clear()`

### Issue: 401 Unauthorized on API calls
- Token may have expired (2-hour expiry)
- Try logging out and back in
- Check token format: should start with `eyJ`

---

## 📚 Documentation

**For complete details, read:**
- `INTEGRATION_GUIDE.md` - Full integration documentation
- `INTEGRATION_REPORT.md` - Integration summary and status

---

## ✅ Checklist

- [ ] Backend running on 8088
- [ ] Frontend running on 5173
- [ ] MySQL running on 3306
- [ ] Database `pharmacy-app` exists
- [ ] Can sign up as patient
- [ ] Can sign up as pharmacy
- [ ] Can login and get redirected
- [ ] Token appears in localStorage
- [ ] Protected routes work
- [ ] API calls appear in Network tab

---

## 🎯 Next Steps

Ready to implement:
1. ClientDashboard real data
2. PharmacyDashboard real data
3. Medicine search functionality
4. Reservation creation/management

Just let me know and I can integrate those features!

---

**Last Updated**: April 20, 2026
**Status**: ✅ Ready to Use
