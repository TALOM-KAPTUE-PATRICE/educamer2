import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Assignment } from '../../../../../core/models/instructor.model';


@Component({
  selector: 'app-add-assignment-dialog',
  standalone: false,
  templateUrl: './add-assignment-dialog.component.html',
  styleUrl: './add-assignment-dialog.component.scss'
})
export class AddAssignmentDialogComponent implements OnInit {


  assignmentForm!: FormGroup;
  selectedFile: File | null = null;
  fileName: string = '';

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<AddAssignmentDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { assignment?: Assignment }
  ) { }

  ngOnInit(): void {
    this.assignmentForm = this.fb.group({
      title: [this.data.assignment?.title || '', Validators.required],
      description: [this.data.assignment?.description || '', Validators.required],
      dueDate: [this.data.assignment?.dueDate || null]
    });
  }

  onFileSelected(event: Event) {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      this.selectedFile = file;
      this.fileName = file.name;
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

    removeFile(event: Event): void {
    event.stopPropagation(); // Empêche l'ouverture de la sélection de fichier
    this.selectedFile = null;
    this.fileName = '';
  }

  onSave(): void {
    if (this.assignmentForm.valid) {
      this.dialogRef.close({
        data: this.assignmentForm.value,
        file: this.selectedFile
      });
    }
  }


}
