import axios from 'axios';

export const userServiceApi = axios.create({
  baseURL: 'http://localhost:8081/api', // Placeholder URL
  headers: {
    'Content-Type': 'application/json',
  },
});

export const orderServiceApi = axios.create({
  baseURL: 'http://localhost:8082/api', // Placeholder URL
  headers: {
    'Content-Type': 'application/json',
  },
});
