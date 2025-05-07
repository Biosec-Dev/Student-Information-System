import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TeacherService {
  constructor(private http: HttpClient) { }

  getProfile(): Observable<any> {
    return this.http.get<any>('/api/teachers/profile');
  }

  getCourses(): Observable<any[]> {
    return this.http.get<any[]>('/api/teachers/courses');
  }

  getAdvisees(): Observable<any[]> {
    return this.http.get<any[]>('/api/teachers/advisees');
  }

  getPendingEnrollments(): Observable<any[]> {
    return this.http.get<any[]>('/api/teachers/courses/pending-enrollments');
  }

  approveCourse(studentCourseId: number): Observable<any> {
    return this.http.post<any>(`/api/teachers/courses/approve?studentCourseId=${studentCourseId}`, {});
  }
} 