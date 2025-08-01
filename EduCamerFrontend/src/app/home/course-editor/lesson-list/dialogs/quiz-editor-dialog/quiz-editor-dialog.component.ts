import { Component, Inject, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Quiz, Question, Answer } from '../../../../../core/models/instructor.model';

@Component({
  selector: 'app-quiz-editor-dialog',
  standalone: false,
  templateUrl: './quiz-editor-dialog.component.html',
  styleUrls: ['./quiz-editor-dialog.component.scss']
})
export class QuizEditorDialogComponent implements OnInit {
  quizForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<QuizEditorDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { lessonId: number; quiz: Quiz | null }
  ) {}

  ngOnInit(): void {
    this.quizForm = this.fb.group({
      title: [this.data.quiz?.title || '', Validators.required],
      questions: this.fb.array([])
    });

    if (this.data.quiz?.questions && this.data.quiz.questions.length > 0) {
      this.data.quiz.questions.forEach(q => this.addQuestion(q));
    } else {
      this.addQuestion(); // Ajouter une première question vide
    }
  }

  // Accesseurs pour un template plus propre
  get questions(): FormArray {
    return this.quizForm.get('questions') as FormArray;
  }

  answers(questionIndex: number): FormArray {
    return this.questions.at(questionIndex).get('answers') as FormArray;
  }

  // Méthodes pour manipuler le FormArray
  addQuestion(questionData?: Question): void {
    const questionForm = this.fb.group({
      id: [questionData?.id || null],
      text: [questionData?.text || '', Validators.required],
      answers: this.fb.array([])
    });

    const answersArray = questionForm.get('answers') as FormArray;
    if (questionData?.answers && questionData.answers.length > 0) {
      questionData.answers.forEach(a => answersArray.push(this.createAnswer(a)));
    } else {
      answersArray.push(this.createAnswer());
      answersArray.push(this.createAnswer());
    }
    this.questions.push(questionForm);
  }

  addAnswer(questionIndex: number): void {
    this.answers(questionIndex).push(this.createAnswer());
  }

  createAnswer(answerData?: Answer): FormGroup {
    return this.fb.group({
      id: [answerData?.id || null],
      text: [answerData?.text || '', Validators.required],
      isCorrect: [answerData?.isCorrect || false]
    });
  }

  removeQuestion(index: number): void {
    this.questions.removeAt(index);
  }

  removeAnswer(questionIndex: number, answerIndex: number): void {
    this.answers(questionIndex).removeAt(answerIndex);
  }

  // Logique pour s'assurer qu'une seule réponse est correcte par question
  handleCorrectAnswerChange(questionIndex: number, selectedAnswerIndex: number): void {
    this.answers(questionIndex).controls.forEach((control, i) => {
      const isCorrectControl = (control as FormGroup).get('isCorrect');
      if (i === selectedAnswerIndex) {
        isCorrectControl?.setValue(true);
      } else {
        isCorrectControl?.setValue(false, { emitEvent: false });
      }
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}