import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../auth.service';


export const passwordMatchValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const password = control.get('newPassword');
  const confirmPassword = control.get('confirmPassword');
  return password && confirmPassword && password.value !== confirmPassword.value ? { passwordMismatch: true } : null;
};


@Component({
  selector: 'app-reset-password',
  standalone: false,
  templateUrl: './reset-password.component.html',
  styleUrls: ['../auth-shared.scss', './reset-password.component.scss']
})
export class ResetPasswordComponent {


  resetPasswordForm!: FormGroup;
  token: string | null = null;
  isSubmitting = false;

    // NOUVEAU: Gérer l'état de la validation du token
  isLoading = true; // Affiche un spinner au début
  isTokenValid = false; // N'affiche le formulaire que si le token est valide

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token');
    this.resetPasswordForm = this.fb.group({
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    }, { validators: passwordMatchValidator });

    if (!this.token) {
      this.handleInvalidToken("Lien de réinitialisation invalide : token manquant.");
      return;
    }

    // On valide le token auprès du backend avant de faire quoi que ce soit
    this.authService.validateResetToken(this.token).subscribe({
      next: () => {
        // Le token est valide, on peut afficher le formulaire
        this.isTokenValid = true;
        this.isLoading = false;
      },
      error: () => {
        // Le backend a renvoyé une erreur (400), le token est invalide
        this.handleInvalidToken("Le lien de réinitialisation est invalide ou a expiré.");
      }
    });
  }
  

  onSubmit(): void {
    if (this.resetPasswordForm.invalid || this.isSubmitting || !this.token) {
      return;
    }
    this.isSubmitting = true;
    const payload = {
      token: this.token,
      newPassword: this.resetPasswordForm.value.newPassword
    };

    this.authService.resetPassword(payload).subscribe({
      next: (message: string) => { // Typer la réponse
        this.isSubmitting = false;
        this.snackBar.open(message, 'OK', { panelClass: 'success-snackbar' });
        this.router.navigate(['/auth']);
      },
      error: (err) => {
        // L'intercepteur gère déjà, mais on peut personnaliser
        this.isSubmitting = false;
        // Le message d'erreur du backend est maintenant géré par l'intercepteur.
      }
    });
  }

  private handleInvalidToken(message: string): void {
    this.snackBar.open(message, 'OK', { duration: 7000, panelClass: 'error-snackbar' });
    this.router.navigate(['/auth']);
    this.isLoading = false;
    this.isTokenValid = false;
  }
}
