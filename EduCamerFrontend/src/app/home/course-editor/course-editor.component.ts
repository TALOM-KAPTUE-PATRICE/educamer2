import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { InstructorService } from '../../core/services/instructor.service';
import { Course, CourseEditorData } from '../../core/models/instructor.model';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-course-editor',
  standalone: false,
  templateUrl: './course-editor.component.html',
  styleUrls: ['./course-editor.component.scss']

})
export class CourseEditorComponent implements OnInit {
  courseId!: number;
  courseData: CourseEditorData | null = null;
  isPublishing = false; // Pour gérer l'état du bouton


  constructor(
    private route: ActivatedRoute,
    private instructorService: InstructorService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.courseId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.courseId) {
      this.loadCourseDetails();
    }
  }

  loadCourseDetails(): void {
    this.instructorService.getCourseForEditor(this.courseId).subscribe(data => {
      this.courseData = data;
    });
  }

  publishCourse(): void {
    if (this.courseId && this.courseData?.status === 'DRAFT') {
      this.isPublishing = true;
      this.instructorService.publishCourse(this.courseId).subscribe({
        next: (updatedCourse: Course) => {
          if (this.courseData) {
            // ▼▼▼ LA CORRECTION EST ICI ▼▼▼
            // On met à jour uniquement les propriétés pertinentes de this.courseData
            // sans écraser les listes de leçons et de devoirs.
            this.courseData.status = updatedCourse.status;
            // On pourrait mettre à jour d'autres champs si l'API les renvoyait
            // this.courseData.title = updatedCourse.title; 
          }
          this.snackBar.open('Cours publié avec succès !', 'Fermer', { duration: 3000 });
          this.isPublishing = false;
        },
        error: (err) => {
          // L'intercepteur gère déjà les messages d'erreur génériques,
          // mais on peut être plus spécifique ici.
          const errorMessage = err.error?.message || 'Erreur lors de la publication du cours.';
          this.snackBar.open(errorMessage, 'Fermer', { duration: 5000, panelClass: 'error-snackbar' });
          this.isPublishing = false;
        }
      });
    }
  }

  onContentUpdated(): void {
    this.snackBar.open('Mise à jour réussie. Rechargement des données...', 'Fermer', { duration: 2000 });
    this.loadCourseDetails();
  }
}