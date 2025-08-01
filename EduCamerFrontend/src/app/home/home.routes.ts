import { Routes } from '@angular/router';
import { DashboardLayoutComponent } from '../layouts/dashboard-layout/dashboard-layout.component';
import { roleGuard } from '../core/guards/role.guard';

// Import des composants de page
import { DashboardComponent } from './dashboard/dashboard.component';
import { MyCoursesComponent } from './my-courses/my-courses.component';
import { CatalogComponent } from './catalog/catalog.component';
import { MyGradesComponent } from './my-grades/my-grades.component';
import { CourseManagementComponent } from './course-management/course-management.component';
import { AdminPanelComponent } from './admin-panel/admin-panel.component';
import { ProfileComponent } from './profile/profile.component';
import { ApplicationDetailComponent } from './admin-panel/application-detail/application-detail.component';
import { CourseEditorComponent } from './course-editor/course-editor.component';
import { CourseViewComponent } from './course-view/course-view.component';
import { CourseTrackingComponent } from './course-tracking/course-tracking.component';
import { ForumThreadComponent } from './forum/forum-thread/forum-thread.component';
import { ForumComponent } from './forum/forum.component';
import { ParentDashboardComponent } from './parent-dashboard/parent-dashboard.component';

export const HOME_ROUTES: Routes = [
  {
    path: '',
    component: DashboardLayoutComponent,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardComponent },

      // --- Route Commune ---
      { path: 'profile', component: ProfileComponent }, // Accessible par tous les utilisateurs connectés

      // --- Routes pour l'ÉLÈVE ---
      { path: 'my-courses', component: MyCoursesComponent, canActivate: [roleGuard], data: { expectedRoles: ['STUDENT'] } },
      { path: 'catalog', component: CatalogComponent, canActivate: [roleGuard], data: { expectedRoles: ['STUDENT', 'PARENT'] } },
      { path: 'my-grades', component: MyGradesComponent, canActivate: [roleGuard], data: { expectedRoles: ['STUDENT'] } },

      // --- Routes pour l'INSTRUCTEUR ---
      { path: 'course-management', 
        component: CourseManagementComponent, 
        canActivate: [roleGuard], 
        data: { 
          expectedRoles: ['INSTRUCTOR', 'ADMIN'],
          breadcrumb: 'Gestion des Cours' 
        } 
      },

      // --- Routes pour l'ADMINISTRATEUR ---
      { path: 'admin-panel', component: AdminPanelComponent, canActivate: [roleGuard], data: { expectedRoles: ['ADMIN'] } },
      {
        path: 'admin-panel/application/:id',
        component: ApplicationDetailComponent, // à créer
        canActivate: [roleGuard],
        data: { expectedRoles: ['ADMIN'] }
      },

      {
        path: 'course-editor/:id', // :id est un paramètre dynamique
        component: CourseEditorComponent,
        canActivate: [roleGuard],
        data: { expectedRoles: ['INSTRUCTOR', 'ADMIN'],
        breadcrumb: 'Éditeur de Cours' 
        }
      },

      {
        path: 'course-view/:id',
        component: CourseViewComponent,
        canActivate: [roleGuard],
        data: { expectedRoles: ['STUDENT'] }
      },

      {
        path: 'course-tracking/:id', // :id est l'ID du cours
        component: CourseTrackingComponent,
        canActivate: [roleGuard],
        data: { expectedRoles: ['INSTRUCTOR', 'ADMIN'] }
      },
      {
        path: 'course/:courseId/forum',
        component: ForumComponent,
        canActivate: [roleGuard],
        data: { expectedRoles: ['STUDENT', 'INSTRUCTOR', 'ADMIN'] }
      },
      // Route pour voir un fil de discussion spécifique
      {
        path: 'forum/thread/:threadId',
        component: ForumThreadComponent,
        canActivate: [roleGuard],
        data: { expectedRoles: ['STUDENT', 'INSTRUCTOR', 'ADMIN'] }
      },

      {
        path: 'parent-dashboard',
        component: ParentDashboardComponent,
        canActivate: [roleGuard],
        data: { expectedRoles: ['PARENT'] }
      }

      // ... Ajoutez les autres routes pour Tuteur, Parent, etc. ici
    ]
  }
];