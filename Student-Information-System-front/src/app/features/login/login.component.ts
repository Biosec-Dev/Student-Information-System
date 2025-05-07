import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';
import { CommonModule } from '@angular/common';
import { RouterService } from '../../core/services/router.service';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule]
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  loading = false;
  submitted = false;
  error = '';
  returnUrl: string;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private routerService: RouterService,
    private authService: AuthService
  ) {
    // Redirect if already logged in
    if (this.authService.isLoggedIn()) {
      this.redirectBasedOnRole();
    }

    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });

    // Get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  ngOnInit(): void {
  }

  // Convenience getter for easy access to form fields
  get f() { return this.loginForm.controls; }

  onSubmit(): void {
    this.submitted = true;

    // Stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.authService.login(this.f['email'].value, this.f['password'].value)
      .subscribe({
        next: (response) => {
          if (response) {
            this.redirectBasedOnRole();
          } else {
            this.error = 'Invalid credentials';
            this.loading = false;
          }
        },
        error: error => {
          console.error('Login error details:', error);
          
          if (error.status === 0) {
            this.error = 'Server is unreachable. Check your network connection.';
          } else if (error.status === 401) {
            this.error = 'Invalid username or password';
          } else if (error.error && typeof error.error === 'string') {
            this.error = error.error;
          } else {
            this.error = `Login failed: ${error.message || 'Unknown error'}`;
          }
          
          this.loading = false;
        }
      });
  }

  private redirectBasedOnRole(): void {
    if (this.authService.isStudent()) {
      this.routerService.navigateTo(['/student']);
    } else if (this.authService.isTeacher()) {
      this.routerService.navigateTo(['/teacher']);
    } else {
      this.routerService.navigateTo(['/']);
    }
  }
} 