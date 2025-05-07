import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpInterceptorFn
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../auth/auth.service';
import { inject } from '@angular/core';

// Functional interceptor for newer Angular versions
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();
  
  // Skip adding token for login request
  if (req.url.includes('/api/auth/login')) {
    return next(req);
  }

  // Clone the request and add the authorization header if we have a token
  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(authReq);
  }

  return next(req);
};

// Class-based interceptor for compatibility with HTTP_INTERCEPTORS
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    // Get the auth token from the service
    const token = this.authService.getToken();

    // Skip adding token for login request
    if (request.url.includes('/api/auth/login')) {
      return next.handle(request);
    }

    // Clone the request and add the authorization header if we have a token
    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(request);
  }
} 