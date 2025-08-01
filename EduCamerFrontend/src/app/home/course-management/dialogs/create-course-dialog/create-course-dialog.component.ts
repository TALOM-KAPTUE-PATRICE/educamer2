import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-create-course-dialog',
  standalone: false,
  templateUrl: './create-course-dialog.component.html',
  styleUrls: ['./create-course-dialog.component.scss']
})
export class CreateCourseDialogComponent implements OnInit {
  courseForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<CreateCourseDialogComponent>
  ) {}

  ngOnInit(): void {
    this.courseForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(5)]],
      description: ['', [Validators.required, Validators.minLength(20)]]
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}