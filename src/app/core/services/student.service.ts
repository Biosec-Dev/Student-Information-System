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
    console.log('Student service: Fetching available courses');
    // First, get both active and pending courses
    return forkJoin({
      active: this.getActiveCourses().pipe(catchError(() => of([]))),
      pending: this.getPendingCourses().pipe(catchError(() => of([]))),
      all: this.http.get<any[]>('/api/courses').pipe(
        map(courses => {
          console.log('Raw courses from API:', courses);
          return courses;
        }),
        catchError(() => of([]))
      )
    }).pipe(
      map(({ active, pending, all }) => {
        console.log('All courses from API:', all.length);
        console.log('Active courses:', active.length);
        console.log('Pending courses:', pending.length);
        
        // Create a set of course IDs that the student is already enrolled in or has requested
        const enrolledCourseIds = new Set([
          ...active.map(course => course.id),
          ...pending.map(course => course.id)
        ]);
        
        console.log('Enrolled or pending course IDs:', Array.from(enrolledCourseIds));
        
        // Filter out courses that the student is already enrolled in or has requested
        const availableCourses = all
          .filter(course => !enrolledCourseIds.has(course.id))
          .map(course => ({
            id: course.id,
            name: course.name,
            courseNo: course.courseNo,
            teacher: course.teacher || { name: 'No Teacher', lastName: 'Assigned' }
          }));
          
        console.log('Available courses after filtering:', availableCourses.length);
        return availableCourses;
      }),
      catchError(error => {
        console.error('Error fetching available courses:', error);
        return of([]);
      })
    );
  }

  requestCourse(courseId: number): Observable<string> {
    return this.http.post<string>(`/api/students/courses/request?courseId=${courseId}`, {}, {
      responseType: 'text' as any
    });
  }

  getAvailableAdvisers(): Observable<any[]> {
    return this.http.get<any[]>('/api/students/advisers/available');
  }

  changeAdviser(adviserId: number): Observable<any> {
    return this.http.post<any>(`/api/students/adviser?adviserId=${adviserId}`, {});
  }
} 