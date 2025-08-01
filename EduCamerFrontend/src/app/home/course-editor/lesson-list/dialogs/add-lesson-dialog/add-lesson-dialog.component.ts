import { Component, Inject, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-add-lesson-dialog',
  standalone: false,
  templateUrl: './add-lesson-dialog.component.html',
  styleUrl: './add-lesson-dialog.component.scss'
})
export class AddLessonDialogComponent implements OnInit {

    lessonForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<AddLessonDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { nextOrder: number }
  ) {}

  ngOnInit(): void {
    this.lessonForm = this.fb.group({
      title: ['', Validators.required],
      content: ['', Validators.required],
      lessonOrder: [this.data.nextOrder, Validators.required]
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

}
