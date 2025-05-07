import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-test-dropdown',
  template: `
    <div class="container mt-5">
      <h2>API Test: Available Courses</h2>
      
      <div class="mb-3">
        <button (click)="fetchCourses()" class="btn btn-primary">Fetch Courses</button>
      </div>
      
      <div *ngIf="loading" class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
      
      <div *ngIf="error" class="alert alert-danger">
        Error: {{error}}
      </div>
      
      <div *ngIf="courses.length > 0">
        <h3>Raw Course Data ({{courses.length}} courses):</h3>
        <pre>{{coursesJson}}</pre>
        
        <h3>Dropdown Test:</h3>
        <select class="form-select">
          <option value="">Select a course</option>
          <option *ngFor="let course of courses" [value]="course.id">
            {{course.courseNo}} - {{course.name}} (Teacher: {{course.teacher?.name || 'None'}})
          </option>
        </select>
      </div>
      
      <div *ngIf="courses.length === 0 && !loading && !error" class="alert alert-info">
        No courses available or API returned empty result.
      </div>
    </div>
  `,
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class TestDropdownComponent implements OnInit {
  courses: any[] = [];
  loading = false;
  error = '';
  coursesJson = '';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.fetchCourses();
  }

  fetchCourses() {
    this.loading = true;
    this.error = '';
    this.courses = [];
    
    this.http.get<any[]>('/api/courses')
      .subscribe({
        next: (data) => {
          this.courses = data || [];
          this.coursesJson = JSON.stringify(data, null, 2);
          this.loading = false;
          console.log('Courses loaded:', this.courses.length);
          console.log('Course data:', this.courses);
        },
        error: (err) => {
          this.error = err.message || 'Failed to load courses';
          this.loading = false;
          console.error('Error fetching courses:', err);
        }
      });
  }
} 