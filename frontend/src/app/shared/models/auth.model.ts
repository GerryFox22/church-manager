export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
}

export type UserRole = 'ADMIN' | 'USER';

export interface JwtPayload {
  sub: string;
  role: UserRole;
  exp: number;
  iat: number;
}
