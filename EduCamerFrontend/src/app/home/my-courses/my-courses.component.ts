import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { StudentService } from '../../core/services/student.service';
import { EnrolledCourseSummary } from '../../core/models/student.model';

@Component({
  selector: 'app-my-courses',
  standalone: false,
  templateUrl: './my-courses.component.html',
  styleUrl: './my-courses.component.scss'

})
export class MyCoursesComponent implements OnInit {

  myCourses$!: Observable<EnrolledCourseSummary[]>; // Remplacer 'any' par un modèle fort

  constructor(private studentService: StudentService) {}

  ngOnInit(): void {
    this.myCourses$ = this.studentService.getMyEnrolledCourses();
  }

}
