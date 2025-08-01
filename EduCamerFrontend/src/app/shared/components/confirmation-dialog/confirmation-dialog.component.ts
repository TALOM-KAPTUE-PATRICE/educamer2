import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-confirmation-dialog',
  standalone: false,
  // templateUrl: './confirmation-dialog.component.html',
  // styleUrl: './confirmation-dialog.component.scss' 
   template: `
    <h1 mat-dialog-title>{{ data.title }}</h1>
    <div mat-dialog-content>
      <p>{{ data.message }}</p>
    </div>
    <div mat-dialog-actions align="end">
      <button mat-button [mat-dialog-close]="false">Annuler</button>
      <button mat-flat-button [color]="data.confirmButtonColor || 'primary'" [mat-dialog-close]="true" cdkFocusInitial>
        {{ data.confirmButtonText || 'Confirmer' }}
      </button>
    </div>
  `
     
})
export class ConfirmationDialogComponent {
    constructor(
    public dialogRef: MatDialogRef<ConfirmationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { 
      title: string, 
      message: string, 
      confirmButtonText?: string,
      confirmButtonColor?: 'primary' | 'accent' | 'warn' 
    }
  ) {}

}
