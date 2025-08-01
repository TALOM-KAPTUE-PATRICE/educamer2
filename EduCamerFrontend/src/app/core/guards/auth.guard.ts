import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../../auth/auth.service';
import { map, take } from 'rxjs/operators';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.isAuthenticated$.pipe(
    take(1), // On ne prend que la première valeur émise pour éviter les souscriptions infinies
    map(isAuthenticated => {
      if (isAuthenticated) {
        return true; // L'utilisateur est connecté, il peut accéder à la route
      }
      // L'utilisateur n'est pas connecté, on le redirige vers la page de connexion
      router.navigate(['/auth']);
      return false;
    })
  );
};