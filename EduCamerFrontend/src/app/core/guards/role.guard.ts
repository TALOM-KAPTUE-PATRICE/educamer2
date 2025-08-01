import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../../auth/auth.service';
import { map, take } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const snackBar = inject(MatSnackBar);
  
  // Récupérer les rôles attendus depuis les données de la route
  const expectedRoles = route.data['expectedRoles'] as Array<string>;

  return authService.currentUser$.pipe(
    take(1),
    map(user => {
      if (!user || !expectedRoles.includes(user.role)) {
        snackBar.open('Accès non autorisé pour votre rôle.', 'Fermer', { duration: 5000 });
        router.navigate(['/home/dashboard']); // Redirige vers le dashboard principal en cas d'échec
        return false;
      }
      return true; // L'utilisateur a le bon rôle
    })
  );
};