import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { jwtDecode } from 'jwt-decode'; // Installer avec: npm install jwt-decode
import { MatSnackBar } from '@angular/material/snack-bar';
import { environment } from '../../environments/environment';



// Interface pour la réponse du backend après connexion
export interface AuthResponse {
  token: string;
  id: number;
  name: string;
  email: string;
  role: string;
  avatarUrl?: string; // <-- AJOUTER
}

// Interface pour les informations utilisateur décodées du JWT
export interface DecodedToken {
  sub: string; // email
  role: string;
  permissions: string[];
  exp: number;
}

// Interface pour l'utilisateur courant dans l'application
export interface CurrentUser {
  id: number;
  name: string;
  email: string;
  role: string;
  permissions: string[];
  avatarUrl?: string; // <-- AJOUTEZ CETTE PROPRIÉTÉ (optionnelle avec ?)
}



@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
   private apiUrl = `${environment.apiUrl}/auth`;  // Le proxy de Render s'en chargera

  // Sujets pour diffuser l'état de l'utilisateur et de l'authentification
  private currentUserSubject = new BehaviorSubject<CurrentUser | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();


  // --- NOUVEAU GETTER PUBLIC ---
  /**
   * Retourne la valeur actuelle de l'utilisateur connecté de manière synchrone.
   * Utile dans les cas où l'on a besoin de la valeur immédiatement sans souscrire à l'observable.
   */
  public get currentUserValue(): CurrentUser | null {
    return this.currentUserSubject.value;
  }

  constructor(private http: HttpClient, private router: Router, private snackBar: MatSnackBar) {
    this.loadUserFromStorage();
  }

  // === Méthodes publiques principales ===

  public login(credentials: any): Observable<any> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => this.handleAuthentication(response)),
      catchError(this.handleError)
    );
  }


  public register(userInfo: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/signup`, userInfo).pipe(
      catchError(this.handleError)
    );
  }


  public logout(): void {
    // On notifie le backend pour invalider le token
    this.http.post(
      `${this.apiUrl}/logout`,
      {},
      { responseType: 'text' }

    ).subscribe({
      next: (message: string) => {
        // Afficher le message de succès du backend dans une SnackBar
        this.snackBar.open(message, 'OK', {
          duration: 3000,
          panelClass: ['success-snackbar'], // Appliquer un style de succès
          verticalPosition: 'top'
        });
        this.clearSession();
      },
      error: err => {
        console.error('Erreur lors de la déconnexion backend, mais on déconnecte quand même localement.', err);
        // On affiche un message générique et on déconnecte quand même
        this.snackBar.open('Déconnexion effectuée.', 'OK', {
          duration: 3000,
          verticalPosition: 'top'
        });
        this.clearSession();
      }
    });
  }


  // === Utilitaires de gestion du token et de l'état ===

  public getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  public hasPermission(requiredPermission: string): boolean {
    const user = this.currentUserSubject.value;
    return user?.permissions.includes(requiredPermission) ?? false;
  }


  private loadUserFromStorage(): void {
    const token = this.getToken();
    const storedUser = localStorage.getItem('currentUser');

    if (token && storedUser) {
      try {
        const decodedToken: DecodedToken = jwtDecode(token);
        const isExpired = Date.now() >= decodedToken.exp * 1000;

        if (isExpired) {
          this.clearSession();
        } else {
          // Si le token est valide, on restaure l'état de l'application
          const user: CurrentUser = JSON.parse(storedUser);
          this.currentUserSubject.next(user);
          this.isAuthenticatedSubject.next(true);
        }
      } catch (error) {
        console.error('Erreur lors de la validation du token stocké:', error);
        this.clearSession();
      }
    }
  }

  // === Cœur de la logique améliorée ===
  private handleAuthentication(response: AuthResponse): void {
    // 1. Décoder le token pour obtenir les permissions
    const decodedToken: DecodedToken = jwtDecode(response.token);

    // 2. Créer l'objet CurrentUser complet avec les infos de la réponse de login
    const user: CurrentUser = {
      id: response.id,
      name: response.name,
      email: response.email,
      role: response.role,
      permissions: decodedToken.permissions || [],
      avatarUrl: response.avatarUrl // <-- AJOUTER CETTE LIGNE
    };

    // 3. Stocker le token ET l'objet utilisateur
    localStorage.setItem('authToken', response.token);
    localStorage.setItem('currentUser', JSON.stringify(user));

    // 4. Mettre à jour les observables
    this.currentUserSubject.next(user);
    this.isAuthenticatedSubject.next(true);

    // 5. Rediriger
    this.navigateToDashboard(user.role);
  }

  private clearSession(): void {
    localStorage.removeItem('authToken');
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    this.isAuthenticatedSubject.next(false);
    this.router.navigate(['/auth']);
  }

  private navigateToDashboard(role: string): void {
    // Si la route /home est la route principale pour le dashboard
    this.router.navigate(['/home']);
    // Vous pourriez avoir une logique plus complexe ici
    // if (role === 'ADMINISTRATEUR') { this.router.navigate(['/admin']); } ...
  }

  private handleError(error: any): Observable<never> {
    // L'intercepteur d'erreurs gère déjà l'affichage.
    // On peut logger ici si besoin.
    console.error('Erreur API dans AuthService:', error);
    // On propage l'erreur pour que le composant puisse réagir (ex: arrêter un spinner)
    throw error;
  }

  /**
 * Met à jour l'état de l'utilisateur courant dans toute l'application.
 * Cette méthode est appelée par d'autres services (comme UserService) après une mise à jour réussie.
 * @param updatedProfile Les données partielles ou complètes du profil mis à jour.
 */
  public updateCurrentUser(updatedProfile: Partial<CurrentUser>): void {
    const currentUser = this.currentUserSubject.value;
    if (currentUser) {
      // Fusionne l'ancien utilisateur avec les nouvelles données
      const newUserState = { ...currentUser, ...updatedProfile };
      // Met à jour le localStorage et le BehaviorSubject
      localStorage.setItem('currentUser', JSON.stringify(newUserState));
      this.currentUserSubject.next(newUserState);
    }
  }

  /**
 * Envoie une demande de réinitialisation de mot de passe au backend.
 * Le backend renvoie un message texte simple.
 */
  forgotPassword(payload: { email: string }): Observable<string> {
    return this.http.post(`${this.apiUrl}/forgot-password`, payload, { responseType: 'text' });
  }

  /**
   * Envoie le nouveau mot de passe et le token de validation au backend.
   * Le backend renvoie un message texte simple.
   */
  resetPassword(payload: { token: string, newPassword: string }): Observable<string> {
    return this.http.post(`${this.apiUrl}/reset-password`, payload, { responseType: 'text' });
  }
  /**
     * Vérifie la validité d'un token de réinitialisation auprès du backend.
  */
  validateResetToken(token: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/validate-reset-token`, { params: { token } });
  }


}