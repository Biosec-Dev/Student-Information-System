import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, forkJoin } from 'rxjs';
import { map, catchError, switchMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  constructor(private http: HttpClient) { }

  getProfile(): Observable<any> {
    return this.http.get<any>('/api/students/profile');
  }

  getActiveCourses(): Observable<any[]> {
    return this.http.get<any[]>('/api/students/courses/active');
  }

  getPendingCourses(): Observable<any[]> {
    return this.http.get<any[]>('/api/students/courses/pending');
  }

  getAvailableCourses(): Observable<any[]> {
    // First, get both active and pending courses
    return forkJoin({
      active: this.getActiveCourses(),
      pending: this.getPendingCourses(),
      all: this.http.get<any[]>('/api/courses')
    }).pipe(
      map(({ active, pending, all }) => {
        // Create a set of course IDs that the student is already enrolled in or has requested
        const enrolledCourseIds = new Set([
          ...active.map(course => course.id),
          ...pending.map(course => course.id)
        ]);
        
        // Filter out courses that the student is already enrolled in or has requested
        // and courses without a teacher
        return all
          .filter(course => course.teacher != null && !enrolledCourseIds.has(course.id))
          .map(course => ({
            id: course.id,
            name: course.name,
            courseNo: course.courseNo,
            teacher: course.teacher || { name: 'No Teacher', lastName: 'Assigned' }
          }));
      }),
      catchError(error => {
        console.error('Error fetching available courses:', error);
        return of([]);
      })
    );
  }

  requestCourse(courseId: number): Observable<any> {
    return this.http.post<any>(`/api/students/courses/request?courseId=${courseId}`, {});
  }

  getAvailableAdvisers(): Observable<any[]> {
    return this.http.get<any[]>('/api/students/advisers/available');
  }

  changeAdviser(adviserId: number): Observable<any> {
    return this.http.post<any>(`/api/students/adviser?adviserId=${adviserId}`, {});
  }
} 