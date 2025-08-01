import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-forgot-password',
  standalone: false,
  templateUrl: './forgot-password.component.html',
  styleUrls: ['../auth-shared.scss', './forgot-password.component.scss']
})
export class ForgotPasswordComponent {


  forgotPasswordForm!: FormGroup;
  isSubmitting = false;
  isSubmitted = false;


  constructor(private fb: FormBuilder, private authService: AuthService) { }

  
  ngOnInit(): void {
    this.forgotPasswordForm = this.fb.group({ email: ['', [Validators.required, Validators.email]] });
  }

  onSubmit(): void {
    if (this.forgotPasswordForm.invalid || this.isSubmitting) {
      return;
    }
    this.isSubmitting = true;
    this.authService.forgotPassword(this.forgotPasswordForm.value).subscribe({
      next: () => {
        this.isSubmitted = true;
        this.isSubmitting = false;
      },
      error: () => {
        // Même en cas d'erreur, on affiche un message de succès pour ne pas révéler si un email existe
        this.isSubmitted = true;
        this.isSubmitting = false;
      }
    });
  }

}
