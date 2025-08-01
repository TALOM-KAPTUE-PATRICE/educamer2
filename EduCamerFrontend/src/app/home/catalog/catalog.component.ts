import { Component, OnInit } from '@angular/core';
import { StudentService } from '../../core/services/student.service';
import { Category, CatalogCourse } from '../../core/models/student.model';
import { Observable, BehaviorSubject, switchMap, tap } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-catalog',
  standalone: false,
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.scss']

})
export class CatalogComponent implements OnInit {
  categories$!: Observable<Category[]>;
  isParent: boolean = false;

    // NOUVEAU: Stocker les IDs des cours auxquels l'élève est déjà inscrit
  enrolledCourseIds = new Set<number>();
  
  // Sujet pour gérer le filtre de catégorie sélectionnée
  private selectedCategorySubject = new BehaviorSubject<number | 'all'>('all');
  
  // Observable qui réagit aux changements de catégorie
  filteredCourses$!: Observable<CatalogCourse[]>;
  
  constructor(
    private studentService: StudentService,
    private snackBar: MatSnackBar,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.isParent = user?.role === 'PARENT';
        // On ne charge les cours inscrits que si c'est un élève
      if (user?.role === 'STUDENT') {
        this.studentService.getMyEnrolledCourses().subscribe(enrolledCourses => {
          this.enrolledCourseIds.clear(); // Vider l'ensemble avant de le remplir
          enrolledCourses.forEach(c => this.enrolledCourseIds.add(c.id));
        });
      }
    });

    this.categories$ = this.studentService.getAllCategories();
    
    this.filteredCourses$ = this.selectedCategorySubject.pipe(
      switchMap(categoryId => {
        if (categoryId === 'all') {
          return this.studentService.getAllCourses();
        } else {
          return this.studentService.getCoursesByCategory(categoryId);
        }
      })
    );
  }

  selectCategory(categoryId: number | 'all'): void {
    this.selectedCategorySubject.next(categoryId);
  }

  enroll(courseId: number, event: Event): void {
    event.stopPropagation(); // Empêche la navigation quand on clique sur le bouton
    event.preventDefault();
    
    this.studentService.enrollInCourse(courseId).subscribe({
      next: () => {

        this.snackBar.open('Inscription réussie !', 'OK', { duration: 3000 });
               // Mettre à jour l'état localement pour une réactivité instantanée
        this.enrolledCourseIds.add(courseId);
      },
      error: (err) => {
        const message = err.error?.message || 'Erreur lors de l\'inscription.';
        this.snackBar.open(message, 'OK', { duration: 5000 });
      }
    });
  }

   // ▼▼▼ MÉTHODE MANQUANTE À AJOUTER ▼▼▼
  isEnrolled(courseId: number): boolean {
    return this.enrolledCourseIds.has(courseId);
  }
  // ▲▲▲ FIN DE L'AJOUT ▲▲▲
}