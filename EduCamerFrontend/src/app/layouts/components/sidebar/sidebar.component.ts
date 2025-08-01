import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService, CurrentUser } from '../../../auth/auth.service';


interface NavLink {
  path: string;
  label: string;
  icon: string;
  requiredPermission?: string; // On se base sur une permission unique
  // Si pas de permission, le lien est visible pour tous les utilisateurs connectés
}

@Component({
  selector: 'app-sidebar',
  standalone: false,
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {
  currentUser$: Observable<CurrentUser | null>;

  // Liste complète des liens avec leurs permissions requises
  allNavLinks: NavLink[] = [
    // Liens Communs
    { path: '/home/dashboard', label: 'Tableau de bord', icon: 'dashboard' },
    { path: '/home/profile', label: 'Mon Profil', icon: 'person' },
    
    // Liens Élève
    { path: '/home/my-courses', label: 'Mes Cours', icon: 'school', requiredPermission: 'course:read_enrolled' },
    { path: '/home/catalog', label: 'Catalogue de Cours', icon: 'storefront', requiredPermission: 'course:read_catalog' },
    { path: '/home/my-grades', label: 'Mes Notes', icon: 'assessment', requiredPermission: 'tracking:read_own' },
    { path: '/home/help', label: 'Demander de l\'aide', icon: 'support_agent', requiredPermission: 'help_request:create' },

    // Liens Instructeur
    { path: '/home/course-management', label: 'Gérer mes Cours', icon: 'create_new_folder', requiredPermission: 'course:create' },
    { path: '/home/instructor-stats', label: 'Statistiques', icon: 'bar_chart', requiredPermission: 'tracking:read_course' },

    { path: '/home/parent-dashboard', label: 'Gestion des enfants', icon: 'gavel', requiredPermission: 'tracking:read_child' },
    // Liens Modération / Tuteur / Admin
    { path: '/home/forum-mod', label: 'Modération Forum', icon: 'gavel', requiredPermission: 'forum:moderate' },
    { path: 'home/tutor-requests', label: 'Demandes d\'aide', icon: 'live_help', requiredPermission: 'help_request:read_all' },
    { path: '/home/admin-panel', label: 'Panel Admin', icon: 'admin_panel_settings', requiredPermission: 'user:manage' },
  ];

  // On injecte AuthService qui contient la logique
  constructor(public authService: AuthService) {
    this.currentUser$ = this.authService.currentUser$;
  }

  // La logique de filtrage est maintenant dans le template HTML avec un *ngFor et un *ngIf
}