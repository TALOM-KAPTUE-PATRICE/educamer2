import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { UserService } from '../../core/services/user.service';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { map } from 'rxjs/operators';
import { AuthService, CurrentUser } from '../../auth/auth.service';
import { Course } from '../../core/models/instructor.model';
import { StudentService } from '../../core/services/student.service';
import { CatalogCourse } from '../../core/models/student.model';


@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {

  trendingCourses$!: Observable<CatalogCourse[]>;
  topRatedCourses$!: Observable<CatalogCourse[]>;

  currentUser$: Observable<CurrentUser | null>;

  // Rendre la grille responsive
  cols$: Observable<number>;


  // Données factices pour les différents rôles
  studentStats = [
    { icon: 'school', value: '3', label: 'Cours en cours', color: 'blue' as const, progressValue: 60 },
    { icon: 'assignment_turned_in', value: '5', label: 'Devoirs terminés', color: 'green' as const },
    { icon: 'military_tech', value: '88%', label: 'Note moyenne', color: 'orange' as const },
    { icon: 'schedule', value: '2', label: 'Devoirs à venir', color: 'purple' as const },
  ];

  instructorStats = [
    { icon: 'group', value: '124', label: 'Élèves actifs', color: 'blue' as const },
    { icon: 'video_library', value: '12', label: 'Cours publiés', color: 'orange' as const },
    { icon: 'forum', value: '15', label: 'Nouvelles questions', color: 'green' as const },
    { icon: 'pending_actions', value: '8', label: 'Devoirs à corriger', color: 'purple' as const },
  ];

  constructor(public authService: AuthService, private breakpointObserver: BreakpointObserver,
    private studentService: StudentService

  ) {
    this.currentUser$ = this.authService.currentUser$;

    this.cols$ = this.breakpointObserver.observe([Breakpoints.XSmall, Breakpoints.Small, Breakpoints.Medium])
      .pipe(
        map(result => {
          if (result.breakpoints[Breakpoints.XSmall]) {
            return 1;
          }
          if (result.breakpoints[Breakpoints.Small]) {
            return 2;
          }
          return 4;
        })
      );


     this.currentUser$ = this.authService.currentUser$;
  
  }

  ngOnInit(): void {
    this.trendingCourses$ = this.studentService.getTrendingCourses();
    this.topRatedCourses$ = this.studentService.getTopRatedCourses();
  }

}
