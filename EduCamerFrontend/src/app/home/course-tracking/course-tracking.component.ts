import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { InstructorService } from '../../core/services/instructor.service';
import { Observable } from 'rxjs';
import { Course } from '../../core/models/instructor.model';

@Component({
  selector: 'app-course-tracking',
  standalone: false,  
  templateUrl: './course-tracking.component.html',
  styleUrls: ['./course-tracking.component.scss']
})
export class CourseTrackingComponent implements OnInit {
  course$!: Observable<Course>;

  constructor(
    private route: ActivatedRoute,
    private instructorService: InstructorService
  ) {}

  ngOnInit(): void {
    const courseId = Number(this.route.snapshot.paramMap.get('id'));
    // On récupère juste les détails de base du cours pour l'en-tête
    this.course$ = this.instructorService.getCourseById(courseId); // Méthode à ajouter
  }
}