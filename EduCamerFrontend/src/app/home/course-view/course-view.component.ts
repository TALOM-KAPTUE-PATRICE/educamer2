import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { StudentService } from '../../core/services/student.service';
import { Observable, BehaviorSubject, switchMap, of, map, shareReplay, tap, Subscription, take } from 'rxjs';
import { Quiz } from '../../core/models/instructor.model';
import { EnrolledCourseDetails, LessonDetails, LessonSummary } from '../../core/models/student.model';
import { TakeQuizDialogComponent } from './dialogs/take-quiz-dialog/take-quiz-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSidenav } from '@angular/material/sidenav';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';



@Component({
  selector: 'app-course-view',
  standalone: false,
  templateUrl: './course-view.component.html',
  styleUrls: ['./course-view.component.scss']
})

export class CourseViewComponent implements OnInit, OnDestroy {
  courseId!: number;
  courseData$!: Observable<EnrolledCourseDetails>;
  isHandset$: Observable<boolean>;
  @ViewChild('sidenav') sidenav!: MatSidenav;
  
  // On garde une copie locale des leçons pour la navigation
  private lessons: LessonSummary[] = [];
  private courseDataSubscription!: Subscription;

  private selectedLessonSummarySubject = new BehaviorSubject<LessonSummary | null>(null);
  lessonDetails$: Observable<LessonDetails | null> = this.selectedLessonSummarySubject.pipe(
    switchMap(summary => {
      if (summary && !summary.isAccessible) {
        this.snackBar.open('Vous devez compléter la leçon précédente pour accéder à celle-ci.', 'OK', { duration: 4000 });
        return of(null); // Ne pas charger la leçon si elle est bloquée
      }
      if (summary) {
        // On charge les détails depuis l'API
        return this.studentService.getLessonDetails(this.courseId, summary.id).pipe(
          map(details => {
            // ▼▼▼ AMÉLIORATION ▼▼▼
            // On enrichit les détails avec les infos qu'on a déjà dans le résumé
            if (details) {
              details.isCompleted = summary.isCompleted;
              details.isAccessible = summary.isAccessible;
            }
            return details;
          })
        );
      }
      return of(null);
    })
  );
  selectedLesson$ = this.selectedLessonSummarySubject.asObservable();

  constructor(
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private studentService: StudentService,
    private snackBar: MatSnackBar,
    private breakpointObserver: BreakpointObserver,
  

  ) {
    this.isHandset$ = this.breakpointObserver.observe(Breakpoints.Handset).pipe(
      map(result => result.matches),
      shareReplay()
    );
  }

  ngOnInit(): void {
    this.courseId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadCourseData();
  }

  loadCourseData(): void {
    this.courseData$ = this.studentService.getEnrolledCourseDetails(this.courseId).pipe(
      map(data => this.processLessonAccessibility(data)),
      tap(data => {
        this.lessons = data.lessons; // Stocker la liste des leçons
        // Sélectionner automatiquement la première leçon accessible
        if (!this.selectedLessonSummarySubject.value) {
          const firstLesson = this.lessons.find(l => l.isAccessible);
          if (firstLesson) {
            this.selectLesson(firstLesson);
          }
        }
      })
    );
  }

  private processLessonAccessibility(course: EnrolledCourseDetails): EnrolledCourseDetails {
    let previousLessonCompleted = true;
    course.lessons.forEach(lesson => {
      lesson.isAccessible = previousLessonCompleted;
      previousLessonCompleted = lesson.isCompleted || !lesson.completionRequired;
    });
    return course;
  }

  selectLesson(lessonSummary: LessonSummary): void {
    if (!lessonSummary.isAccessible) {
      this.snackBar.open('Cette leçon est actuellement bloquée.', 'OK', { duration: 3000 });
      return;
    }
    this.selectedLessonSummarySubject.next(lessonSummary);
    // Fermer la sidebar sur mobile après sélection
    this.isHandset$.pipe(take(1)).subscribe(isHandset => {
      if (isHandset) {
        this.sidenav.close();
      }
    });
  }

  takeQuiz(quiz: Quiz): void {
    const dialogRef = this.dialog.open(TakeQuizDialogComponent, { /* ... */ });
    dialogRef.afterClosed().subscribe(wasSuccessful => {
      if (wasSuccessful) {
        this.snackBar.open('Leçon validée ! Vous pouvez passer à la suite.', 'OK');
        this.loadCourseData(); // Recharger pour mettre à jour les états 'isCompleted'
        this.selectNextLesson();
      }
    });
  }

  private selectNextLesson(): void {
    const currentLesson = this.selectedLessonSummarySubject.value;
    if (this.lessons && currentLesson) {
      const currentIndex = this.lessons.findIndex(l => l.id === currentLesson.id);
      if (currentIndex !== -1 && currentIndex < this.lessons.length - 1) {
        const nextLesson = this.lessons[currentIndex + 1];
        this.selectLesson(nextLesson);
      } else {
        this.snackBar.open('Félicitations, vous avez terminé le cours !', 'Génial !');
      }
    }
  }

  ngOnDestroy(): void {
    if (this.courseDataSubscription) {
      this.courseDataSubscription.unsubscribe();
    }
  }

}  
 
 