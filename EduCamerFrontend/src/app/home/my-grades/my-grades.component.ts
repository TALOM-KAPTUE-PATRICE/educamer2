import { Component, OnInit } from '@angular/core';
import { forkJoin, map, Observable, of, switchMap } from 'rxjs';
import { StudentService } from '../../core/services/student.service';
import { EnrolledCourseSummary } from '../../core/models/student.model';
// Importer les modèles de notes
import { StudentGradebook } from '../../core/models/instructor.model';

// Interface pour combiner les données
interface CourseWithGradebook {
  course: EnrolledCourseSummary;
  gradebook: StudentGradebook;
}



@Component({
  selector: 'app-my-grades',
  standalone: false,
  templateUrl: './my-grades.component.html',
  styleUrls: ['./my-grades.component.scss']
})
export class MyGradesComponent implements OnInit {
  // Un seul observable qui gèrera tout le chargement
  coursesWithGrades$!: Observable<CourseWithGradebook[]>;
  isLoading = true

  constructor(private studentService: StudentService) {}

  ngOnInit(): void {
    this.coursesWithGrades$ = this.studentService.getMyEnrolledCourses().pipe(
      // 1. On récupère la liste des cours de l'élève
      switchMap(courses => {
        // 2. Si l'élève n'a pas de cours, on retourne un tableau vide
        if (courses.length === 0) {
          return of([]); // 'of' de rxjs
        }
        
        // 3. Pour chaque cours, on crée un appel API pour récupérer ses notes
        const gradebookObservables = courses.map(course => 
          this.studentService.getMyGrades(course.id).pipe(
            // 4. On combine les infos du cours et ses notes
            map(gradebook => ({ course, gradebook }))
          )
        );
        
        // 5. forkJoin exécute tous les appels en parallèle et attend qu'ils soient tous terminés
        return forkJoin(gradebookObservables);
      })
    );
  }
}