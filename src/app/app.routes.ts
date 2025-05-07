import { Routes } from '@angular/router';
import { LoginComponent } from './features/login/login.component';
import { StudentDashboardComponent } from './features/student-dashboard/student-dashboard.component';
import { TeacherDashboardComponent } from './features/teacher-dashboard/teacher-dashboard.component';
import { AuthGuard } from './core/guards/auth.guard';
import { TestDropdownComponent } from './features/test-dropdown.component';

export const routes: Routes = [
  { 
    path: 'login', 
    component: LoginComponent
  },
  { 
    path: 'student', 
    component: StudentDashboardComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ROLE_STUDENT'] }
  },
  { 
    path: 'teacher', 
    component: TeacherDashboardComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ROLE_TEACHER'] }
  },
  { path: 'test-api', component: TestDropdownComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];
