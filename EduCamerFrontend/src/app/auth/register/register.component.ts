import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'] // Partage le même style que login
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  hidePassword = true;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      // On fusionne nom et prénom en un seul champ 'name'
      name: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      role: ['ELEVE', [Validators.required]] // Ajout du champ pour le rôle
    });
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      return;
    }
    
    this.isLoading = true;
    const { name, email, password, role } = this.registerForm.value;

    const signUpRequest = { name, email, password, role };
      
    this.authService.register(signUpRequest).subscribe({
      next: () => {
        this.isLoading = false;
        this.snackBar.open('Inscription réussie ! Vous pouvez maintenant vous connecter.', 'OK', { duration: 5000 });
        this.router.navigate(['/auth']);
      },
      error: (err) => { 
        this.isLoading = false;
        // L'intercepteur affiche déjà une erreur, mais on peut logger l'erreur brute ici
        console.error('Erreur d\'inscription:', err);
      }
    });
  }
  
}