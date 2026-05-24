import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { API_ENDPOINTS, AUTH_TOKEN_KEY } from '../constants/api.constants';
import {
  AuthResponse,
  JwtPayload,
  LoginRequest,
  UserRole,
} from '../../shared/models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);

  readonly isAuthenticated = signal(this.hasToken());

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${environment.apiUrl}${API_ENDPOINTS.auth.login}`, credentials)
      .pipe(
        tap((response) => {
          this.setToken(response.token);
          this.isAuthenticated.set(true);
        }),
      );
  }

  logout(): void {
    sessionStorage.removeItem(AUTH_TOKEN_KEY);
    this.isAuthenticated.set(false);
    void this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return sessionStorage.getItem(AUTH_TOKEN_KEY);
  }

  getRole(): UserRole | null {
    const token = this.getToken();
    if (!token) {
      return null;
    }

    try {
      return this.parseJwt(token).role ?? null;
    } catch {
      return null;
    }
  }

  isAdmin(): boolean {
    return this.getRole() === 'ADMIN';
  }

  private setToken(token: string): void {
    sessionStorage.setItem(AUTH_TOKEN_KEY, token);
  }

  private hasToken(): boolean {
    const token = this.getToken();
    if (!token) {
      return false;
    }

    try {
      const { exp } = this.parseJwt(token);
      return exp * 1000 > Date.now();
    } catch {
      return false;
    }
  }

  private parseJwt(token: string): JwtPayload {
    const base64 = token.split('.')[1]?.replace(/-/g, '+').replace(/_/g, '/');
    if (!base64) {
      throw new Error('Invalid JWT');
    }

    return JSON.parse(atob(base64)) as JwtPayload;
  }
}
