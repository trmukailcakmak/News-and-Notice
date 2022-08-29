import http from '../http-common';
import authHeader from './auth-header';

class UserService {
  getPublicContent() {
    return http.get('/api/test/all');
  }

  getUserBoard() {
    return http.get('/api/test/user', { headers: authHeader() });
  }

  getTestBoard() {
    return http.get('/api/test/mod', { headers: authHeader() });
  }

  getAdminBoard() {
    return http.get('/api/test/admin', { headers: authHeader() });
  }
}

export default new UserService();
