import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { BecomeInstructorComponent } from '../../become-instructor/become-instructor.component'


// Interface pour les données passées au dialogue
export interface ApplicationReviewData {
  personalInfo: {
    name: string;
    email: string;
    phone: string;
    desiredRole: 'INSTRUCTOR' | 'TUTOR';
  };
  expertiseInfo: {
    specializations: string;
    motivation: string;
  };
  fileName: string;
  parentComponent: BecomeInstructorComponent; 
}

@Component({
  selector: 'app-application-review-dialog',
  standalone: false,
  templateUrl: './application-review-dialog.component.html',
  styleUrls: ['./application-review-dialog.component.scss']
})
export class ApplicationReviewDialogComponent {

    // On expose la propriété isSubmitting du parent pour le template
  get isSubmitting(): boolean {
    return this.data.parentComponent.isSubmitting;
  }

  constructor(
    public dialogRef: MatDialogRef<ApplicationReviewDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ApplicationReviewData
  ) {}

  onCancel(): void {
    this.dialogRef.close(false); // L'utilisateur annule
  }

  onConfirm(): void {
    this.dialogRef.close(true); // L'utilisateur confirme la soumission
  }
}