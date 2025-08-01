import { Component, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-edit-post-dialog',
  standalone: false,
  templateUrl: './edit-post-dialog.component.html',
  styleUrl: './edit-post-dialog.component.scss'
})
export class EditPostDialogComponent {


  form: FormGroup;
  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<EditPostDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { content: string }
  ) {
    this.form = this.fb.group({
      content: [data.content, Validators.required]
    });
  }

}
