import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BehaviorSubject, Observable } from 'rxjs';
import { ParentService } from '../../core/services/parent.service';
import { LinkedStudent } from '../../core/models/parent.model';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-parent-dashboard',
  standalone: false,
  templateUrl: './parent-dashboard.component.html',
  styleUrls: ['./parent-dashboard.component.scss']
})
export class ParentDashboardComponent implements OnInit {
  linkForm!: FormGroup;
  isLinking = false; // Pour l'état du bouton
  
  // Utiliser un BehaviorSubject pour pouvoir mettre à jour la liste sans recharger la page
  private linkedStudentsSubject = new BehaviorSubject<LinkedStudent[]>([]);
  linkedStudents$ = this.linkedStudentsSubject.asObservable();

  constructor(
    private fb: FormBuilder,
    private parentService: ParentService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.linkForm = this.fb.group({
      studentEmail: ['', [Validators.required, Validators.email]],
      linkingCode: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]]
    });
    this.loadLinkedStudents();
  }

  loadLinkedStudents(): void {
    this.parentService.getLinkedStudents().subscribe(students => {
      this.linkedStudentsSubject.next(students);
    });
  }

  onLinkStudent(): void {
    if (this.linkForm.invalid) return;
    this.isLinking = true;
    
    // On appelle le service avec l'objet complet
    this.parentService.linkStudent(this.linkForm.value).subscribe({
      next: (newlyLinkedStudent) => {
        this.snackBar.open(`${newlyLinkedStudent.name} a été lié à votre compte !`, 'OK', { duration: 4000 });
        this.linkForm.reset();
        Object.keys(this.linkForm.controls).forEach(key => {
          this.linkForm.get(key)?.setErrors(null) ;
        });
        const currentStudents = this.linkedStudentsSubject.value;
        this.linkedStudentsSubject.next([...currentStudents, newlyLinkedStudent]);
        this.isLinking = false;
      },
      error: (err) => {
        this.snackBar.open(err.error?.message || 'Erreur lors de la liaison.', 'OK');
        this.isLinking = false;
      }
    });
  }
}