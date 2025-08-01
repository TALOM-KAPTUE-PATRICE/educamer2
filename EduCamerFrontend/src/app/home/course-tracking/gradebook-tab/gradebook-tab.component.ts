import { Component, Input, OnInit } from '@angular/core';
import { InstructorService } from '../../../core/services/instructor.service';
import { ParentService } from '../../../core/services/parent.service'; // Importer
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators'; // Importer
import { StudentGradebook } from '../../../core/models/instructor.model';

@Component({
  selector: 'app-gradebook-tab',
  standalone: false,
  templateUrl: './gradebook-tab.component.html',
  styleUrls: ['./gradebook-tab.component.scss']
})
export class GradebookTabComponent implements OnInit {
  // Entrées pour définir le contexte
  @Input() courseId!: number;
  @Input() studentId?: number; // Optionnel : si présent, on est dans le contexte "Parent"

  gradebookData$!: Observable<StudentGradebook[]>;

  constructor(
    private instructorService: InstructorService,
    private parentService: ParentService // Injecter
  ) {}

  ngOnInit(): void {
    if (this.studentId) {
      // --- Contexte PARENT ---
      // On appelle le service du parent pour ne récupérer que le carnet de notes de l'enfant.
      // Le service renvoie un seul objet, on le met dans un tableau pour que le template fonctionne.
      this.gradebookData$ = this.parentService.getChildGradebook(this.studentId, this.courseId).pipe(
        map(singleGradebook => [singleGradebook])
      );
    } else {
      // --- Contexte INSTRUCTEUR ---
      // On appelle le service de l'instructeur pour récupérer les carnets de notes de tous les élèves.
      this.gradebookData$ = this.instructorService.getCourseGradebook(this.courseId);
    }
  }
}