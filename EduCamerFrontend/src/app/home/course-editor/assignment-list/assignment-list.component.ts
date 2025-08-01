import { Component, EventEmitter, Input, Output } from '@angular/core'; // <-- Assurez-vous d'importer Input
import { Assignment } from '../../../core/models/instructor.model'; // <-- Importez le modèle
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { InstructorService } from '../../../core/services/instructor.service';
import { AddAssignmentDialogComponent } from './dialogs/add-assignment-dialog/add-assignment-dialog.component';
import { ConfirmDialogComponent } from '../../../dialogs/confirm-dialog/confirm-dialog.component';
import { AssignmentDetailsDialogComponent } from './dialogs/assignment-details-dialog/assignment-details-dialog.component';

@Component({
  selector: 'app-assignment-list',
  standalone: false,
  templateUrl: './assignment-list.component.html',
  styleUrls: ['./assignment-list.component.scss']
})
export class AssignmentListComponent {

  @Input() assignments: Assignment[] = [];
  @Input() courseId?: number;
  @Output() contentUpdated = new EventEmitter<void>();
    // NOUVEAU: Définir les colonnes pour la table
  displayedColumns: string[] = ['title', 'dueDate', 'actions'];
  @Input() isEditable: boolean = false; // NOUVEAU

  constructor(
    private dialog: MatDialog,
    private instructorService: InstructorService,
    private snackBar: MatSnackBar
  ) {}

  addAssignment(): void {

    if (!this.courseId) {
      console.error("Impossible d'ajouter un devoir: l'ID du cours est manquant.");
      return; // Sortir de la fonction si courseId n'est pas fourni
    }

    const courseId = this.courseId; // On assigne à une constante pour que TypeScript comprenne

    const dialogRef = this.dialog.open(AddAssignmentDialogComponent, { width: '600px', data: {} });


    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.instructorService.addAssignment(courseId, result.data, result.file).subscribe({
          next: () => {
            this.snackBar.open('Devoir ajouté avec succès !', 'OK');
            this.contentUpdated.emit();
          },
          error: () => this.snackBar.open('Erreur lors de l\'ajout du devoir.', 'OK')
        });
      }
    });
  }

    viewDetails(assignment: Assignment): void {
    const dialogRef = this.dialog.open(AssignmentDetailsDialogComponent, {
      width: '700px',
      data: { assignment }
    });

    // Optionnel: si l'utilisateur clique sur "Modifier" depuis la vue détaillée,
    // on ouvre directement la dialogue d'édition.
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'edit') {
        this.editAssignment(assignment);
      }
    });
  }

  editAssignment(assignment: Assignment): void {
    const dialogRef = this.dialog.open(AddAssignmentDialogComponent, { width: '600px', data: { assignment } });
    
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.instructorService.updateAssignment(assignment.id, result.data).subscribe({
          next: () => {
            this.snackBar.open('Devoir mis à jour.', 'OK');
            this.contentUpdated.emit();
          },
          error: () => this.snackBar.open('Erreur lors de la mise à jour.', 'OK')
        });
      }
    });
  }
  
  deleteAssignment(assignment: Assignment): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Confirmer la suppression',
        message: `Voulez-vous vraiment supprimer le devoir "${assignment.title}" ? Cette action est irréversible.`
      }
    });

    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.instructorService.deleteAssignment(assignment.id).subscribe({
          next: () => {
            this.snackBar.open('Devoir supprimé avec succès.', 'OK', { duration: 3000 });
            this.contentUpdated.emit(); // Notifier pour recharger
          },
          error: () => this.snackBar.open('Erreur lors de la suppression du devoir.', 'OK')
        });
      }
    });
  }
  
}