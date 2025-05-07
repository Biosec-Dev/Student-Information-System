import { Component, OnInit } from '@angular/core';
import { TeacherService } from '../../core/services/teacher.service';
import { AuthService } from '../../core/auth/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterService } from '../../core/services/router.service';

@Component({
  selector: 'app-teacher-dashboard',
  templateUrl: './teacher-dashboard.component.html',
  imports: [CommonModule, FormsModule, HttpClientModule],
  standalone: true
})
export class TeacherDashboardComponent implements OnInit {
  profile: any;
  courses: any[] = [];
  filteredCourses: any[] = [];
  advisees: any[] = [];
  filteredAdvisees: any[] = [];
  pendingEnrollments: any[] = [];
  
  // Filter inputs
  courseNameFilter: string = '';
  courseNoFilter: string = '';
  studentNameFilter: string = '';
  studentEmailFilter: string = '';
  
  loading = false;
  loadingEnrollments = false;
  error = '';
  successMessage = '';

  constructor(
    private teacherService: TeacherService,
    private authService: AuthService,
    private routerService: RouterService
  ) { }

  ngOnInit(): void {
    this.loadProfile();
    this.loadCourses();
    this.loadAdvisees();
    this.loadPendingEnrollments();
  }

  loadProfile(): void {
    this.teacherService.getProfile().subscribe({
      next: (data) => {
        this.profile = data;
      },
      error: (error) => {
        this.error = 'Failed to load profile';
        console.error(error);
      }
    });
  }

  loadCourses(): void {
    this.teacherService.getCourses().subscribe({
      next: (data) => {
        this.courses = data;
        this.applyCoursesFilter();
      },
      error: (error) => {
        this.error = 'Failed to load courses';
        console.error(error);
      }
    });
  }

  loadAdvisees(): void {
    this.loading = true;
    this.teacherService.getAdvisees().subscribe({
      next: (data) => {
        this.advisees = data;
        this.applyAdviseesFilter();
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load advisees';
        this.loading = false;
        console.error(error);
      }
    });
  }

  loadPendingEnrollments(): void {
    this.loadingEnrollments = true;
    this.teacherService.getPendingEnrollments().subscribe({
      next: (data) => {
        this.pendingEnrollments = data;
        this.loadingEnrollments = false;
      },
      error: (error) => {
        this.error = 'Failed to load pending enrollments';
        this.loadingEnrollments = false;
        console.error(error);
      }
    });
  }

  // Filter methods
  applyCoursesFilter(): void {
    this.filteredCourses = this.courses.filter(course => {
      const courseName = course.name?.toLowerCase() || '';
      const courseNo = course.courseNo?.toLowerCase() || '';
      
      return (this.courseNameFilter === '' || courseName.includes(this.courseNameFilter.toLowerCase())) &&
             (this.courseNoFilter === '' || courseNo.includes(this.courseNoFilter.toLowerCase()));
    });
  }

  applyAdviseesFilter(): void {
    this.filteredAdvisees = this.advisees.filter(student => {
      const studentName = `${student.name || ''} ${student.lastName || ''}`.toLowerCase();
      const studentEmail = student.email?.toLowerCase() || '';
      
      return (this.studentNameFilter === '' || studentName.includes(this.studentNameFilter.toLowerCase())) &&
             (this.studentEmailFilter === '' || studentEmail.includes(this.studentEmailFilter.toLowerCase()));
    });
  }

  // Filter update handlers
  onCoursesFilterChange(): void {
    this.applyCoursesFilter();
  }

  onAdviseesFilterChange(): void {
    this.applyAdviseesFilter();
  }

  // Clear filters
  clearCoursesFilter(): void {
    this.courseNameFilter = '';
    this.courseNoFilter = '';
    this.applyCoursesFilter();
  }

  clearAdviseesFilter(): void {
    this.studentNameFilter = '';
    this.studentEmailFilter = '';
    this.applyAdviseesFilter();
  }

  approveCourse(studentCourseId: number): void {
    this.loading = true;
    this.error = '';
    this.successMessage = '';

    this.teacherService.approveCourse(studentCourseId).subscribe({
      next: () => {
        this.successMessage = 'Course enrollment approved';
        this.loading = false;
        this.loadPendingEnrollments(); // Reload pending enrollments
        this.loadAdvisees(); // Reload advisees to update the list
      },
      error: (error) => {
        this.error = error.error || 'Failed to approve course';
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