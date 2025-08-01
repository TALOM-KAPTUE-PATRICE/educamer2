import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { EnrolledCourseSummary } from '../../../../core/models/student.model';
import { StudentService } from '../../../../core/services/student.service';
import { StudentProgress } from '../../../../core/models/instructor.model';
import { ParentService } from '../../../../core/services/parent.service';

@Component({
  selector: 'app-child-progress-dialog',
  standalone: false,
  templateUrl: './child-progress-dialog.component.html',
  styleUrl: './child-progress-dialog.component.scss'
})
export class ChildProgressDialogComponent implements OnInit {

  enrolledCourses$!: Observable<EnrolledCourseSummary[]>;
  progressDataMap: { [courseId: number]: Observable<StudentProgress> } = {};

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { studentId: number, studentName: string },
    private studentService: StudentService,
    private parentService: ParentService
  ) {}

  ngOnInit(): void {
    // 1. Récupérer les cours de l'élève
    this.enrolledCourses$ = this.parentService.getEnrolledCoursesForChild(this.data.studentId);
  }

  // 2. Méthode pour charger la progression à la demande
  getStudentProgress(courseId: number): Observable<StudentProgress> {
    if (!this.progressDataMap[courseId]) {
      this.progressDataMap[courseId] = this.parentService.getChildProgress(this.data.studentId, courseId);
    }
    return this.progressDataMap[courseId];
  }

}
