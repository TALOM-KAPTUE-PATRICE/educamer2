import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { StudentService } from '../../core/services/student.service'; // Ajouter la méthode au service
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-help',
  standalone: false,
  templateUrl: './help.component.html',
  styleUrls: ['./help.component.scss']
})
export class HelpComponent {
  helpForm: FormGroup;

  constructor(
    private fb: FormBuilder, 
    private studentService: StudentService,
    private snackBar: MatSnackBar
  ) {
    this.helpForm = this.fb.group({
      subject: ['', [Validators.required, Validators.maxLength(200)]],
      description: ['', [Validators.required, Validators.minLength(20)]]
    });
  }

  onSubmit(): void {
    if (this.helpForm.valid) {
      this.studentService.createHelpRequest(this.helpForm.value).subscribe({
        next: () => {
          this.snackBar.open('Votre demande a été envoyée ! Un tuteur vous répondra bientôt.', 'OK');
          this.helpForm.reset();
        },
        error: () => this.snackBar.open('Erreur lors de l\'envoi de la demande.', 'OK')
      });
    }
  }
}