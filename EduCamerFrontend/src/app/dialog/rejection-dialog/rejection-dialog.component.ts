import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-rejection-dialog',
  standalone: false,
  templateUrl: './rejection-dialog.component.html',
  styleUrls: ['./rejection-dialog.component.scss']
})
export class RejectionDialogComponent {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<RejectionDialogComponent>
  ) {
    this.form = this.fb.group({
      reason: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  onCancel(): void {
    this.dialogRef.close(); // Ferme le dialogue sans renvoyer de données
  }

  onConfirm(): void {
    if (this.form.valid) {
      // Ferme le dialogue et renvoie la raison au composant parent
      this.dialogRef.close({ reason: this.form.value.reason });
    }
  }
}