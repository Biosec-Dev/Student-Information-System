import { Component, OnInit } from '@angular/core';
import { StudentService } from '../../core/services/student.service';
import { AuthService } from '../../core/auth/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterService } from '../../core/services/router.service';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-student-dashboard',
  templateUrl: './student-dashboard.component.html',
  imports: [CommonModule, FormsModule, HttpClientModule],
  standalone: true
})
export class StudentDashboardComponent implements OnInit {
  profile: any;
  activeCourses: any[] = [];
  filteredActiveCourses: any[] = [];
  pendingCourses: any[] = [];
  availableCourses: any[] = [];
  selectedCourseId: number | null = null;
  teacherFilter: string = '';
  courseNameFilter: string = '';
  loading = false;
  error = '';
  successMessage = '';

  constructor(
    private studentService: StudentService,
    private authService: AuthService,
    private routerService: RouterService
  ) { }

  ngOnInit(): void {
    this.loadProfile();
    this.loadActiveCourses();
    this.loadPendingCourses();
    this.loadAvailableCourses();
  }

  loadProfile(): void {
    this.studentService.getProfile().subscribe({
      next: (data) => {
        this.profile = data;
      },
      error: (error) => {
        this.error = 'Failed to load profile';
        console.error(error);
      }
    });
  }

  loadActiveCourses(): void {
    this.studentService.getActiveCourses().subscribe({
      next: (data) => {
        this.activeCourses = data;
        this.applyFilters();
      },
      error: (error) => {
        this.error = 'Failed to load active courses';
        console.error(error);
      }
    });
  }

  loadPendingCourses(): void {
    this.studentService.getPendingCourses().subscribe({
      next: (data) => {
        this.pendingCourses = data;
      },
      error: (error) => {
        this.error = 'Failed to load pending courses';
        console.error(error);
      }
    });
  }

  loadAvailableCourses(): void {
    this.studentService.getAvailableCourses().subscribe({
      next: (data) => {
        this.availableCourses = data;
      },
      error: (error) => {
        this.error = 'Failed to load available courses';
        console.error(error);
      }
    });
  }

  applyFilters(): void {
    this.filteredActiveCourses = this.activeCourses.filter(course => {
      const teacherName = `${course.teacher.name} ${course.teacher.lastName}`.toLowerCase();
      const courseName = course.name.toLowerCase();
      
      return (this.teacherFilter === '' || teacherName.includes(this.teacherFilter.toLowerCase())) &&
             (this.courseNameFilter === '' || courseName.includes(this.courseNameFilter.toLowerCase()));
    });
  }

  onFilterChange(): void {
    this.applyFilters();
  }

  requestCourse(): void {
    if (!this.selectedCourseId) {
      this.error = 'Please select a course';
      return;
    }

    this.loading = true;
    this.error = '';
    this.successMessage = '';

    this.studentService.requestCourse(this.selectedCourseId).subscribe({
      next: () => {
        this.successMessage = 'Course request submitted';
        this.loading = false;
        this.loadPendingCourses();
        this.loadAvailableCourses();
        this.selectedCourseId = null;
      },
      error: (error) => {
        this.error = error.error || 'Failed to request course';
        this.loading = false;
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.routerService.navigateTo(['/login']);
  }
  
  refreshPage(): void {
    this.routerService.refreshCurrentPage();
  }
} 