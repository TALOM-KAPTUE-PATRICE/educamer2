import { Component, Inject, Input, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { LinkedStudent } from '../../../../core/models/parent.model';
import { ChildProgressDialogComponent } from '../child-progress-dialog/child-progress-dialog.component';
import { Observable } from 'rxjs';
import { EnrolledCourseSummary } from '../../../../core/models/student.model';
import { StudentService } from '../../../../core/services/student.service';
import { ParentService } from '../../../../core/services/parent.service';

@Component({
  selector: 'app-child-grades-dialog',
  standalone: false,
  templateUrl: './child-grades-dialog.component.html',
  styleUrl: './child-grades-dialog.component.scss'
})
export class ChildGradesDialogComponent implements OnInit {

  enrolledCourses$!: Observable<EnrolledCourseSummary[]>;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { studentId: number, studentName: string },
    private parentService: ParentService
  ) { }

  ngOnInit(): void {
    // Récupérer la liste des cours de l'enfant pour construire l'accordéon
    this.enrolledCourses$ = this.parentService.getEnrolledCoursesForChild(this.data.studentId);
  }



}
