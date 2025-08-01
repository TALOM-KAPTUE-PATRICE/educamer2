import { Component, OnInit } from '@angular/core';
import { InstructorService } from '../../core/services/instructor.service';
import { CreateCourseDialogComponent } from './dialogs/create-course-dialog/create-course-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Course } from '../../core/models/instructor.model';
import { ConfirmationDialogComponent } from '../../shared/components/confirmation-dialog/confirmation-dialog.component';
// import { MatDialog } from '@angular/material/dialog';
// import { CreateCourseDialogComponent } from './dialogs/create-course-dialog.component';

@Component({
  selector: 'app-course-management',
  standalone: false,
  templateUrl: './course-management.component.html',
  styleUrls: ['./course-management.component.scss']
})
export class CourseManagementComponent implements OnInit {
  myCourses: Course[] = []; // Remplacez 'any' par une interface Course forte
  publishedCourses: Course[] = [];
  draftCourses: Course[] = [];

  constructor(
    private instructorService: InstructorService,
   private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadMyCourses();
  }

  loadMyCourses(): void {
    this.instructorService.getMyCourses().subscribe(courses => {
            // On sépare les cours en deux listes
      this.publishedCourses = courses.filter(c => c.status === 'PUBLISHED');
      this.draftCourses = courses.filter(c => c.status === 'DRAFT');
      
    });
  }

  openCreateCourseDialog(): void {
    const dialogRef = this.dialog.open(CreateCourseDialogComponent, {
      width: '500px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.instructorService.createCourse(result).subscribe({
          next: () => {
            this.snackBar.open('Cours créé avec succès !', 'Fermer', { duration: 3000 });
            this.loadMyCourses(); // Recharger la liste
          },
          error: (err) => {
            this.snackBar.open('Erreur lors de la création du cours.', 'Fermer', { duration: 3000 });
          }
        });
      }
    });
  }

    deleteCourse(courseId: number, courseTitle: string): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: {
        title: 'Confirmer la suppression',
        message: `Êtes-vous sûr de vouloir supprimer le cours "${courseTitle}" ? Cette action est irréversible et supprimera toutes les leçons, devoirs et soumissions associés.`,
        confirmButtonText: 'Supprimer',
        confirmButtonColor: 'warn'
      }
    });

    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.instructorService.deleteCourse(courseId).subscribe({
          next: () => {
            this.snackBar.open('Cours supprimé avec succès.', 'OK', { duration: 3000 });
            this.loadMyCourses(); // Recharger la liste
          },
          error: () => this.snackBar.open('Erreur lors de la suppression du cours.', 'OK')
        });
      }
    });
  }


}