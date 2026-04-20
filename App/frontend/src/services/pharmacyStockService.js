/**
 * Pharmacy Stock Service
 * Handles inventory/stock related API calls
 */

import apiClient from './apiClient';

const pharmacyStockService = {
  /**
   * Get all stock (Admin only)
   */
  getAllStock: async () => {
    const response = await apiClient.get('/pharmacy-stock/all');
    return response;
  },

  /**
   * Get stock item by ID
   */
  getStockById: async (stockId) => {
    const response = await apiClient.get(`/pharmacy-stock/${stockId}`);
    return response;
  },

  /**
   * Get pharmacy inventory (Pharmacy/Admin)
   */
  getPharmacyInventory: async (pharmacyId) => {
    const response = await apiClient.get(`/pharmacy-stock/pharmacy/${pharmacyId}`);
    return response;
  },

  /**
   * Get stock by drug ID
   */
  getStockByDrug: async (drugId) => {
    const response = await apiClient.get(`/pharmacy-stock/drug/${drugId}`);
    return response;
  },

  /**
   * Add stock item
   */
  addStock: async (stockData) => {
    const response = await apiClient.post('/pharmacy-stock', {
      pharmacyId: stockData.pharmacyId,
      drugId: stockData.drugId,
      quantity: stockData.quantity,
      price: stockData.price,
      reservationDelayMinutes: stockData.reservationDelayMinutes || 24,
    });
    return response;
  },

  /**
   * Update stock quantity/price
   */
  updateStock: async (stockId, stockData) => {
    const response = await apiClient.put(`/pharmacy-stock/${stockId}`, {
      quantity: stockData.quantity,
      price: stockData.price,
      reservationDelayMinutes: stockData.reservationDelayMinutes,
    });
    return response;
  },

  /**
   * Delete stock item
   */
  deleteStock: async (stockId) => {
    const response = await apiClient.delete(`/pharmacy-stock/${stockId}`);
    return response;
  },
};

export default pharmacyStockService;
