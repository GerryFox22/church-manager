export const API_ENDPOINTS = {
  auth: {
    login: '/auth/login',
    register: '/auth/register',
  },
  news: '/news',
  events: '/events',
} as const;

export const AUTH_TOKEN_KEY = 'churchmanager_auth_token';
