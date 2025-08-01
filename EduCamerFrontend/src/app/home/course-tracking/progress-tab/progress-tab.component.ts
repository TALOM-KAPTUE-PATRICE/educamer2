import { Component, Input, OnInit } from '@angular/core';
import { InstructorService } from '../../../core/services/instructor.service';
import { map, Observable } from 'rxjs';
import { StudentProgress } from '../../../core/models/instructor.model';
import { ParentService } from '../../../core/services/parent.service';

@Component({
  selector: 'app-progress-tab',
  standalone: false,  
  templateUrl: './progress-tab.component.html',
  styleUrls: ['./progress-tab.component.scss']
})
export class ProgressTabComponent implements OnInit {
  @Input() courseId!: number;
  @Input() studentId?: number; // Optionnel : si fourni, on affiche pour un seul élève
  progressData$!: Observable<StudentProgress[]>;

  constructor(private instructorService: InstructorService, private parentService: ParentService) {}

  ngOnInit(): void {
    
      if (this.studentId) {
      // Contexte Parent: on ne charge que la progression de l'enfant
      this.progressData$ = this.parentService.getChildProgress(this.studentId, this.courseId).pipe(map(p => [p]));
    } else {
      // Contexte Instructeur: on charge la progression de tous les élèves
      this.progressData$ = this.instructorService.getCourseProgress(this.courseId);
    }

  }
}