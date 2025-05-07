import { Injectable } from '@angular/core';
import { Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {
  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (this.authService.isLoggedIn()) {
      // Check if route is restricted by role
      if (route.data['roles'] && route.data['roles'].length) {
        const userRole = this.authService.currentUserValue.role;
        
        // Check if user has required role
        if (!route.data['roles'].includes(userRole)) {
          // Role not authorized, redirect to home page
          this.router.navigate(['/']);
          return false;
        }
      }
      
      // Authorized, proceed
      return true;
    }

    // Not logged in, redirect to login page
    this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }
} 