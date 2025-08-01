import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-create-thread-dialog',
  standalone: false,
  templateUrl: './create-thread-dialog.component.html',
  styleUrl: './create-thread-dialog.component.scss'
})
export class CreateThreadDialogComponent {

  threadForm!: FormGroup;
  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<CreateThreadDialogComponent>
  ) {}

  ngOnInit(): void {
    this.threadForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(10),Validators.maxLength(150)]],
      firstPostContent: ['', [Validators.required, Validators.minLength(20)]]
    });
  }
  onCancel(): void {
    this.dialogRef.close();
  }

}
