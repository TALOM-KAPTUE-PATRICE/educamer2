import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Lesson, Resource } from '../../../core/models/instructor.model';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { InstructorService } from '../../../core/services/instructor.service';
import { AddLessonDialogComponent } from './dialogs/add-lesson-dialog/add-lesson-dialog.component';
import { QuizEditorDialogComponent } from './dialogs/quiz-editor-dialog/quiz-editor-dialog.component';
import { ConfirmationDialogComponent } from '../../../shared/components/confirmation-dialog/confirmation-dialog.component';



@Component({
  selector: 'app-lesson-list',
  standalone: false,
  templateUrl: './lesson-list.component.html',
  styleUrls: ['./lesson-list.component.scss']
})

export class LessonListComponent {
  @Input() lessons: Lesson[] = [];
  @Input() courseId!: number;
  @Output() contentUpdated = new EventEmitter<void>(); // Renommé pour la cohérence

  constructor(
    private dialog: MatDialog,
    private instructorService: InstructorService,
    private snackBar: MatSnackBar
  ) { }



  addLesson(): void {
    const nextOrder = this.lessons.length + 1;
    const dialogRef = this.dialog.open(AddLessonDialogComponent, {
      width: '600px',
      data: { nextOrder: nextOrder }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.instructorService.addLesson(this.courseId, result).subscribe({
          next: () => {
            this.snackBar.open('Leçon ajoutée avec succès !', 'OK', { duration: 3000 });
            this.contentUpdated.emit(); // Notifier le parent de recharger
          },
          error: () => this.snackBar.open('Erreur lors de l\'ajout de la leçon.', 'OK', { duration: 5000 })
        });
      }
    });
  }

  drop(event: CdkDragDrop<Lesson[]>) {

    const newLessons = [...this.lessons]; // Créer une copie pour la manipulation
    // Déplacer l'élément dans le tableau local pour un retour visuel immédiat
    moveItemInArray(this.lessons, event.previousIndex, event.currentIndex);

    // Créer la liste des IDs dans le nouvel ordre
    const orderedIds = this.lessons.map(lesson => lesson.id);

    // Mettre à jour l'ordre sur le backend
    this.instructorService.updateLessonOrder(this.courseId, orderedIds).subscribe({
      next: () => {
        this.snackBar.open('Ordre des leçons sauvegardé.', 'OK', { duration: 2000 });
        this.contentUpdated.emit();
      },
      error: () => {
        this.snackBar.open('Erreur lors de la sauvegarde de l\'ordre.', 'OK');
        // Annuler le changement visuel si le backend échoue
        this.contentUpdated.emit();
      }
    });
  }

  /**
   * Gère la mise à jour d'une seule leçon dans la liste.
   * @param updatedLesson La leçon avec sa nouvelle liste de ressources.
   * @param lessonId L'ID de la leçon à remplacer.
   */

  onLessonUpdated(updatedLesson: Lesson, lessonId: number): void {
    // On trouve l'index de la leçon à mettre à jour
    const index = this.lessons.findIndex(l => l.id === lessonId);
    if (index !== -1) {
      // On remplace l'ancienne leçon par la nouvelle dans le tableau
      // On crée une nouvelle référence du tableau pour garantir la détection de changement
      const newLessons = [...this.lessons];
      newLessons[index] = updatedLesson;
      this.lessons = newLessons;
    }
  }


  /**
   * Gère la mise à jour d'une seule leçon dans la liste après qu'un de ses enfants
   * (comme ResourceList) a effectué une modification.
   * @param updatedLesson La leçon avec sa nouvelle liste de ressources/quiz.
   * @param originalLessonId L'ID de la leçon à remplacer.
   */
  onChildContentUpdated(updatedLesson: Lesson, originalLessonId: number): void {
    const index = this.lessons.findIndex(l => l.id === originalLessonId);
    if (index !== -1) {
      // On crée une nouvelle référence du tableau pour garantir la détection de changement d'Angular
      const newLessons = [...this.lessons];
      newLessons[index] = updatedLesson;
      this.lessons = newLessons;
    }
  }

  manageQuiz(lesson: Lesson): void {
    const dialogRef = this.dialog.open(QuizEditorDialogComponent, {
      width: '800px',
      data: { lessonId: lesson.id, quiz: lesson.quiz }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.instructorService.createOrUpdateQuiz(lesson.id, result).subscribe({
          next: (updatedLesson) => {
            this.snackBar.open('Quiz sauvegardé avec succès !', 'OK');
            this.onChildContentUpdated(updatedLesson, lesson.id); // On réutilise la même logique de mise à jour
          },
          error: () => this.snackBar.open('Erreur lors de la sauvegarde du quiz.', 'OK')
        });
      }
    });
  }

  deleteLesson(event: MouseEvent, lesson: Lesson): void {
    event.stopPropagation(); // Empêche l'ouverture/fermeture du panneau d'expansion

    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: {
        title: 'Confirmer la suppression',
        message: `Êtes-vous sûr de vouloir supprimer la leçon "${lesson.title}" ? Toutes les ressources et le quiz associés seront également supprimés.`,
        confirmButtonText: 'Supprimer',
        confirmButtonColor: 'warn'
      }
    });

    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.instructorService.deleteLesson(lesson.id).subscribe({
          next: () => {
            this.snackBar.open('Leçon supprimée avec succès.', 'OK', { duration: 3000 });
            this.contentUpdated.emit(); // Notifier le parent de recharger
          },
          error: () => this.snackBar.open('Erreur lors de la suppression de la leçon.', 'OK')
        });
      }
    });
  }
  

}