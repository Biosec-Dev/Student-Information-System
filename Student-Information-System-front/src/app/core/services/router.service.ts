import { Injectable } from '@angular/core';
import { Router, NavigationExtras } from '@angular/router';
import { Location } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class RouterService {
  constructor(
    private router: Router,
    private location: Location
  ) {}

  /**
   * Navigate to a specific route
   */
  navigateTo(path: string[], extras?: NavigationExtras): Promise<boolean> {
    return this.router.navigate(path, extras);
  }

  /**
   * Navigate to a URL
   */
  navigateToUrl(url: string): Promise<boolean> {
    return this.router.navigateByUrl(url);
  }

  /**
   * Go back to previous page
   */
  goBack(): void {
    this.location.back();
  }

  /**
   * Get current route
   */
  getCurrentRoute(): string {
    return this.router.url;
  }

  /**
   * Refresh the current page while preserving route
   */
  refreshCurrentPage(): void {
    const currentUrl = this.router.url;
    this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
      this.router.navigateByUrl(currentUrl);
    });
  }
} 