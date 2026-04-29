import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, map, tap } from 'rxjs';
import { StorageService } from './storage.service';
import { AuthResponse, User } from '../models/user.model';

export interface LoginPayload {
  username: string;
  password: string;
}

export interface RegisterPayload {
  username: string;
  password: string;
  name: string;
  surname: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly http = inject(HttpClient);
  private readonly storage = inject(StorageService);
  baseUrl: string = 'http://192.168.5.73:8080/api';

  private readonly currentUserSubject = new BehaviorSubject<Omit<User, 'password'> | null>(
    this.storage.get<Omit<User, 'password'>>('auth_user')
  );

  readonly currentUser$ = this.currentUserSubject.asObservable();

  login(payload: LoginPayload): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/auth/login`, payload).pipe(
      tap(response => {
        console.log(response)
        console.log(response.data)
          this.storage.set('token', response.data.token),
          this.storage.set('username', response.data.username),
          //this.storage.set('roles', response.data.roles),
          this.currentUserSubject.next(null)
      }
      )
    );
  }

  register(payload: RegisterPayload): Observable<any> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/auth/register`, payload);
  }

  logout(): void {
    this.storage.remove('token');
    this.storage.remove('username');
    this.storage.remove('roles');
    this.currentUserSubject.next(null);
  }

  token(): string | null {
    return this.storage.get<string>('token');
  }

  isAuthenticated(): boolean {
    return !!this.token();
  }

  userRole(): string[] | null {
    return this.storage.get<string[]>('roles');
  }

  isReviewer(): boolean {
    return this.userRole()?.includes('ROLE_REVIEWER')!;
  }

  isWriter(): boolean {
    return this.userRole()?.includes('ROLE_WRITER')!;
  }

  isAdmin(): boolean {
    return this.userRole()?.includes('ROLE_ADMINISTRATOR')!;
  }

  isPlayer(): boolean {
    return this.userRole()?.includes('ROLE_PLAYER')!;
  }

  username(): string | null {
    return this.storage.get<string>('username');
  }

  getCurrentUserNameAndLastName() {
    return this.http.get<AuthResponse>(`${this.baseUrl}/utente/userInfo`);
  }

}
