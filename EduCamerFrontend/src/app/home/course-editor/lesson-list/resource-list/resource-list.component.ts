import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Lesson, Resource } from '../../../../core/models/instructor.model';
import { MatDialog } from '@angular/material/dialog';
import { AddResourceDialogComponent } from '../dialogs/add-resource-dialog/add-resource-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { InstructorService } from '../../../../core/services/instructor.service';
import { ConfirmDialogComponent } from '../../../../dialogs/confirm-dialog/confirm-dialog.component';
import { AuthService } from '../../../../auth/auth.service';
import { FileService } from '../../../../core/services/file.service';

@Component({
  selector: 'app-resource-list',
  standalone: false,
  templateUrl: './resource-list.component.html',
  styleUrls: ['./resource-list.component.scss']
})
export class ResourceListComponent {
  @Input() resources: Resource[] | undefined;
  @Input() lessonId!: number;
  // NOUVEAU: Par défaut, on est en mode lecture seule
  @Input() isEditable: boolean = false;
   @Output() lessonUpdated = new EventEmitter<Lesson>();
 
  @Output() contentUpdated = new EventEmitter<void>(); // On émet un simple événement de mise à jour


  constructor(
    private dialog: MatDialog,
    private instructorService: InstructorService,
    private snackBar: MatSnackBar,
    public authService: AuthService,
    private fileService: FileService
  ) { }

  addResource(): void {
    const dialogRef = this.dialog.open(AddResourceDialogComponent, {
      width: '500px',
      data: { lessonId: this.lessonId }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.instructorService.addResource(this.lessonId, result.data, result.file).subscribe({
          next: (updatedLesson: Lesson) => {
            this.snackBar.open('Ressource ajoutée avec succès !', 'OK', { duration: 3000 });
            // On émet la leçon mise à jour au parent
             this.lessonUpdated.emit(updatedLesson);
          },
          error: () => this.snackBar.open('Erreur lors de l\'ajout de la ressource.', 'OK')
        });
      }
    });
  }

  deleteResource(resourceId: number): void {
    // Bonne pratique : demander une confirmation avant de supprimer
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: 'Êtes-vous sûr de vouloir supprimer cette ressource ?' }
    });

    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.instructorService.deleteResource(resourceId).subscribe({
          next: () => {
            this.snackBar.open('Ressource supprimée.', 'OK', { duration: 3000 });
            this.contentUpdated.emit(); // Émettre l'événement simple
          },
          error: () => this.snackBar.open('Erreur lors de la suppression.', 'OK')
        });
      }
    });
  }

  /**
 * Construit le lien correct pour une ressource.
 * Si la ressource est privée (PDF), il pointe vers notre API de redirection.
 * Sinon, il utilise l'URL directe de Cloudinary.
 * @param resource La ressource à afficher.
 * @returns L'URL à utiliser dans le template.
 */
  getResourceLink(resource: Resource): string {
    // Dans notre modèle backend, l'URL pour les PDF est déjà l'URL interne de l'API.
    // Cette méthode est une sécurité supplémentaire si le frontend doit décider.
    // Le plus simple est de se fier à l'URL fournie par le backend.
    return resource.url;
  }
  handleResourceClick(resource: Resource, event: Event): void {
    // Pour les liens externes, on laisse le comportement par défaut du navigateur
    if (resource.type === 'LINK') {
      return;
    }

    // Pour tous les autres types, on empêche la navigation et on gère le téléchargement
    event.preventDefault();

    if (resource.isPrivate) {
      this.fileService.downloadSecureFile(resource.url, resource.name);
    } else {
      // Pour les fichiers publics (images, videos), on ouvre simplement le lien
      window.open(resource.url, '_blank');
    }
  }



}