import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Assignment, Resource } from '../../../../../core/models/instructor.model';
import { FileService } from '../../../../../core/services/file.service';


@Component({
  selector: 'app-assignment-details-dialog',
  standalone: false,
  templateUrl: './assignment-details-dialog.component.html',
  styleUrls: ['./assignment-details-dialog.component.scss']
})
export class AssignmentDetailsDialogComponent {
  constructor(
     private fileService: FileService,
    public dialogRef: MatDialogRef<AssignmentDetailsDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { assignment: Assignment }
  ) {}

  onEdit(): void {
    // On ferme le dialogue en renvoyant une action "edit"
    this.dialogRef.close('edit');
  }
  download(assignment: Assignment): void {
      // On utilise attachmentUrl et title
      if (assignment.attachmentIsPrivate) {
          this.fileService.downloadSecureFile(assignment.attachmentUrl!, assignment.title);
      } else if (assignment.attachmentUrl) {
          window.open(assignment.attachmentUrl, '_blank');
      }
  }
}