import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../auth/auth.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService, private snackBar: MatSnackBar) { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {      
        let errorMessage = 'Une erreur inattendue est survenue.';
  
        // ▼▼▼ LA LOGIQUE AMÉLIORÉE EST ICI ▼▼▼

        if (error.status === 401) {
          // Si l'erreur 401 vient de la page de login, c'est une erreur de "Bad Credentials".
          // On ne déconnecte PAS, on affiche juste le message.
          if (request.url.includes('/api/auth/login')) {
            errorMessage = error.error?.message || 'Email ou mot de passe incorrect.';
          } else {
            // Si le 401 vient d'une autre page, c'est une session expirée/invalide.
            // C'est SEULEMENT dans ce cas qu'on force la déconnexion.
            errorMessage = 'Votre session a expiré. Veuillez vous reconnecter.';
            this.authService.logout(); // Déconnexion forcée
          }
        }
        else if (error.status === 403) {
          errorMessage = "Accès refusé. Vous n'avez pas les permissions nécessaires.";
        } else if (error.error && error.error.message) {
          errorMessage = error.error.message;
        } else if (error.status === 0) {
          errorMessage = 'Impossible de contacter le serveur. Vérifiez votre connexion internet.';
        }

        // On n'affiche pas de snackbar pour le logout qui échoue, car l'utilisateur est déjà déconnecté.
        if (!request.url.includes('/api/auth/logout')) {
          this.snackBar.open(errorMessage, 'Fermer', {
            duration: 7000,
            panelClass: ['error-snackbar'],
            verticalPosition: 'top'
          });
        }

        return throwError(() => error);
      })
    );
  }
}