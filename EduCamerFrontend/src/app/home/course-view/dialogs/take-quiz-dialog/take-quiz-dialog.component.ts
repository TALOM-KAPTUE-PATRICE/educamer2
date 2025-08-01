import { Component, Inject } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Quiz } from '../../../../core/models/instructor.model';
import { StudentService } from '../../../../core/services/student.service';
import { QuizAttemptResultReview } from '../../../../core/models/student.model';

@Component({
  selector: 'app-take-quiz-dialog',
  standalone: false,
  templateUrl: './take-quiz-dialog.component.html',
  styleUrl: './take-quiz-dialog.component.scss'
})
export class TakeQuizDialogComponent {

  quizForm!: FormGroup;
  quizData: Quiz;
  isSubmitting = false;
  result: QuizAttemptResultReview | null = null;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<TakeQuizDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { quiz: Quiz },
   private studentService: StudentService
  ) {
    this.quizData = data.quiz;
  }

  ngOnInit(): void {
    const controls: { [key: string]: FormControl } = {};
    this.quizData.questions.forEach(q => {
      controls[q.id!.toString()] = new FormControl(null,Validators.required); // un FormControl par question
    });
    this.quizForm = this.fb.group(controls);
  }

  onSubmit(): void {
    if (this.quizForm.invalid) return;
    this.isSubmitting = true;

    const answers: { [key: number]: number } = {};
    for (const questionId in this.quizForm.value) {
      answers[Number(questionId)] = Number(this.quizForm.value[questionId]);
    }
    
    this.studentService.submitQuiz(this.quizData.id, { answers }).subscribe(result => {
      this.result = result;
      this.isSubmitting = false;
      this.quizForm.disable(); // Bloquer le formulaire après soumission
    });
  }

  // Méthodes pour le style des résultats
  getAnswerClass(questionId: number, answerId: number): string {
    if (!this.result) return '';
    const selectedAnswer = this.quizForm.get(questionId.toString())?.value;
    const correctAnswer = this.result.correctAnswers[questionId];

    if (answerId === correctAnswer) return 'correct-answer';
    if (answerId === selectedAnswer && selectedAnswer !== correctAnswer) return 'incorrect-answer';
    return '';
  }

  closeDialog(): void {
    // On renvoie true si le quiz a été réussi
    this.dialogRef.close(this.result && this.result.percentage >= 50);
  }

}
