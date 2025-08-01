import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
// import { AuthService } from '../auth.service'; // Vous devrez créer ce service
import { finalize } from 'rxjs/operators'; // <-- Importer finalize


@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  hidePassword = true;
   isLoading = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid || this.isLoading) {
      return;
    }
    
    this.isLoading = true;
    this.authService.login(this.loginForm.value).pipe(
      // finalize() s'exécute toujours à la fin de l'observable,
      // que ce soit après une réussite (next/complete) ou une erreur (error).
      finalize(() => this.isLoading = false)
    ).subscribe({
      // Pas besoin de 'next' car la redirection est gérée dans le service.
      // Pas besoin de 'error' ici car l'intercepteur gère l'affichage du message
      // et finalize gère l'état du spinner.
    });
  }
}