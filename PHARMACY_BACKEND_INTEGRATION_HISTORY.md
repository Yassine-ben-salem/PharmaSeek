# PharmacyDashboard Backend Integration History

## Created: 2026-04-23
## Updated: 2026-04-23 - Added DONE status

---

## CHANGES MADE

### BACKEND

#### 1. Added `GET /pharmacy-stock` endpoint (PharmacyStockController)
```java
@GetMapping("")
@PreAuthorize("hasAnyRole('PHARMACY')")
public ResponseEntity<List<PharmacyStockDto>> getMyPharmacyStock(Authentication authentication) {
    return ResponseEntity.ok(pharmacyStockService.getMyPharmacyStock(authentication));
}
```

#### 2. Added `getMyPharmacyStock` method (PharmacyStockService)
```java
public List<PharmacyStockDto> getMyPharmacyStock(Authentication authentication) {
    Long pharmacyId = extractAuthenticatedUserId(authentication);
    return getPharmacyStockByPharmacyId(pharmacyId, authentication);
}
```

#### 3. Added `GET /reservations/pharmacy/me` endpoint (ReservationController)
```java
@GetMapping("/pharmacy/me")
@PreAuthorize("hasAnyRole('PHARMACY')")
public ResponseEntity<List<ReservationDto>> getMyPharmacyReservations(Authentication authentication) {
    return ResponseEntity.ok(reservationService.getPharmacyReservationsForPharmacy(authentication));
}
```

#### 4. Added `getPharmacyReservationsForPharmacy` method (ReservationService)
```java
public List<ReservationDto> getPharmacyReservationsForPharmacy(Authentication authentication) {
    Long pharmacyId = resolveAuthenticatedUserId(authentication);
    if (pharmacyRepository.findById(pharmacyId).isEmpty()) {
        throw new IllegalArgumentException("Pharmacy not found");
    }
    return reservationRepository.findByPharmacyId(pharmacyId).stream()
            .map(this::mapWithItems)
            .collect(Collectors.toList());
}
```

#### 5. Added `DONE` status to ALLOWED_STATUSES
```java
private static final Set<String> ALLOWED_STATUSES = Set.of("PENDING", "CONFIRMED", "CANCELLED", "EXPIRED", "DONE");
```

#### 6. Updated status transition rules
```java
if ("CONFIRMED".equals(currentStatus)) {
    return "DONE".equals(targetStatus)
            || "CANCELLED".equals(targetStatus);
}
```

---

## FRONTEND

### File: pharmacyStockService.js
- Already had `getMyInventory()` calling `GET /pharmacy-stock` ✓

### File: reservationService.js
**Added method:**
```javascript
getMyPharmacyReservations: async () => {
  const response = await apiClient.get('/reservations/pharmacy/me');
  return response;
},
```

### File: PharmacyDashboard.jsx
**Updated stats calculation:**
```javascript
const totalGained = reservationsData
    .filter(res => {
        const resDate = new Date(res.reservedAt).toDateString();
        return resDate === today && res.status === 'DONE';
    })
    .reduce((sum, res) => {
        // calculates from res.total or res.items
    }, 0);
```

**Updated reservation table:**
- Status badges for PENDING, CONFIRMED, DONE
- Confirm button for PENDING → CONFIRMED
- Done button for CONFIRMED → DONE

---

## API Flow:
- **Inventory**: `GET /pharmacy-stock` → returns pharmacy's own stock
- **Reservations**: `GET /reservations/pharmacy/me` → returns pharmacy's reservations
- **Total Gained**: Calculated from reservations with status `DONE` today

### Reservation Status Flow:
```
PENDING → CONFIRMED → DONE
    ↓           ↓
CANCELLED    CANCELLED
```

---

## TODO - Still Needed

- [ ] Add quantity update in inventory modal
- [ ] Add drug search/autocomplete when adding inventory
- [ ] Settings tab functionality for pharmacy profile
- [ ] Email/notification integration## 2026-04-23 23:48 - Inventory and Reservations Full Backend Integration
- PharmacyDashboard: Inventory CRUD connected to /pharmacy-stock endpoints (addStock, updateStock, deleteStock)
- PharmacyDashboard: Modal updated to use drugId/quantity/price/delay fields instead of name/category/status
- PharmacyDashboard: Reservations now has action buttons for Confirm/Cancel (PENDING) and Mark Done (CONFIRMED)
- Added drugName field to PharmacyStockDto and PharmacyStockMapper for displaying drug names in inventory table
- Pharmacy can now create new drug + stock in one step: /pharmacy-stock/with-drug endpoint
- Updated DrugDto with: category, manufacturer, barCode fields
- Created CreateStockWithDrugRequest DTO for combined drug+stock creation
- Added addStockWithDrug() frontend method
- Updated PharmacyDashboard: Add Product modal with drug fields (name, category, description, manufacturer, requires prescription)
- Fixed pharmacy profile loading: /auth/me now returns full pharmacy data including phone, operatingHours, lat/long
- Added phone and operatingHours fields to Pharmacy entity/DTO/mapper
- Created V10 migration for pharmacy phone and operating_hours columns
- Fixed latitude/longitude type mismatch: changed BigDecimal to Double in Client, Pharmacy entities and PharmacyDto
- Simplified PharmacyStockMapper by removing toPharmacyStockDtoWithCoords (now included in main mapper)
- Fixed AuthService to convert BigDecimal to Double when creating pharmacy
- Added PharmacyController and PharmacyService for updating pharmacy profile
- Created pharmacyService.js for frontend API calls
- Updated PharmacyDashboard settings: Save Changes button now calls backend and reloads on success
