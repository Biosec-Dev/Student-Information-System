import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';

interface AuthRequest {
  email: string;
  password: string;
}

interface AuthResponse {
  token: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject: BehaviorSubject<any>;
  public currentUser: Observable<any>;
  private isBrowser: boolean;

  constructor(
    private http: HttpClient,
    private router: Router,
    @Inject(PLATFORM_ID) platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
    this.currentUserSubject = new BehaviorSubject<any>(this.getUserFromStorage());
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): any {
    return this.currentUserSubject.value;
  }

  login(email: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('/api/auth/login', { email, password })
      .pipe(
        tap(response => {
          // Store user details and token in local storage
          const user = { email, token: response.token, role: response.role };
          if (this.isBrowser) {
            localStorage.setItem('currentUser', JSON.stringify(user));
          }
          this.currentUserSubject.next(user);
        }),
        catchError(error => {
          console.error('Login error:', error);
          // Don't convert to null, instead throw the error to be handled by the component
          throw error;
        })
      );
  }

  logout(): void {
    // Remove user from local storage
    if (this.isBrowser) {
      localStorage.removeItem('currentUser');
    }
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!this.currentUserValue;
  }

  isStudent(): boolean {
    return this.currentUserValue && this.currentUserValue.role === 'ROLE_STUDENT';
  }

  isTeacher(): boolean {
    return this.currentUserValue && this.currentUserValue.role === 'ROLE_TEACHER';
  }

  getToken(): string | null {
    return this.currentUserValue ? this.currentUserValue.token : null;
  }

  private getUserFromStorage(): any {
    if (this.isBrowser) {
      const storedUser = localStorage.getItem('currentUser');
      return storedUser ? JSON.parse(storedUser) : null;
    }
    return null;
  }
} 