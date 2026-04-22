/**
 * API Client Service
 * Centralized HTTP client for all backend communication
 * Handles authentication, error handling, and request/response transformation
 */

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8088';

class ApiClient {
  constructor() {
    this.baseURL = API_BASE_URL;
    this.defaultHeaders = {
      'Content-Type': 'application/json',
    };
  }

  /**
   * Get stored access token from localStorage
   */
  getAccessToken() {
    return localStorage.getItem('accessToken');
  }

  /**
   * Set access token in localStorage
   */
  setAccessToken(token) {
    if (token) {
      localStorage.setItem('accessToken', token);
    } else {
      localStorage.removeItem('accessToken');
    }
  }

  /**
   * Get authorization headers with JWT token
   */
  getAuthHeaders() {
    const token = this.getAccessToken();
    return token
      ? {
          ...this.defaultHeaders,
          Authorization: `Bearer ${token}`,
        }
      : this.defaultHeaders;
  }

  /**
   * Generic request method
   */
  async request(endpoint, options = {}) {
    const url = `${this.baseURL}${endpoint}`;
    const headers = this.getAuthHeaders();

    try {
      const response = await fetch(url, {
        ...options,
        headers: {
          ...headers,
          ...options.headers,
        },
        credentials: 'include', // Include cookies for refresh token
      });

      // Handle 401 - Unauthorized (token expired)
      if (response.status === 401) {
        this.setAccessToken(null);
        window.location.href = '/login';
        throw new Error('Session expired. Please login again.');
      }

      // Handle response
      let data;
      const contentType = response.headers.get('content-type');
      
      if (contentType && contentType.includes('application/json')) {
        data = await response.json();
      } else {
        data = await response.text();
      }

      if (!response.ok) {
        const error = new Error(data.message || `HTTP Error: ${response.status}`);
        error.status = response.status;
        error.data = data;
        throw error;
      }

      return data;
    } catch (error) {
      console.error(`Request failed: ${endpoint}`, error);
      throw error;
    }
  }

  /**
   * GET request
   */
  get(endpoint, options = {}) {
    return this.request(endpoint, {
      ...options,
      method: 'GET',
    });
  }

  /**
   * POST request
   */
  post(endpoint, body, options = {}) {
    return this.request(endpoint, {
      ...options,
      method: 'POST',
      body: JSON.stringify(body),
    });
  }

  /**
   * PUT request
   */
  put(endpoint, body, options = {}) {
    return this.request(endpoint, {
      ...options,
      method: 'PUT',
      body: JSON.stringify(body),
    });
  }

  /**
   * DELETE request
   */
  delete(endpoint, options = {}) {
    return this.request(endpoint, {
      ...options,
      method: 'DELETE',
    });
  }

  /**
   * PATCH request
   */
  patch(endpoint, body, options = {}) {
    return this.request(endpoint, {
      ...options,
      method: 'PATCH',
      body: JSON.stringify(body),
    });
  }
}

export default new ApiClient();
