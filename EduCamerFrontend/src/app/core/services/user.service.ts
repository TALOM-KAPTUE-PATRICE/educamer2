import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AuthService } from '../../auth/auth.service';
import { environment } from '../../../environments/environment';

// DTO qui correspond à la réponse de l'API /api/user/...
export interface UserProfileResponse {
  id: number;
  name: string;
  email: string;
  role: string;
  avatarUrl?: string;
}

// DTO pour la requête de mise à jour du nom
export interface UpdateProfilePayload {
  name: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = `${environment.apiUrl}/api/user`; 
  

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  /**
   * Met à jour le nom de l'utilisateur.
   */
  updateProfile(payload: UpdateProfilePayload): Observable<UserProfileResponse> {
    return this.http.put<UserProfileResponse>(`${this.apiUrl}/me/profile`, payload).pipe(
      tap(updatedUser => {
        // Met à jour l'état global de l'utilisateur après une réponse réussie
        this.authService.updateCurrentUser(updatedUser);
      })
    );
  }

  /**
   * Envoie le nouveau fichier d'avatar au backend.
   */
  updateAvatar(file: File): Observable<UserProfileResponse> {
    const formData = new FormData();
    formData.append('file', file, file.name);

    return this.http.post<UserProfileResponse>(`${this.apiUrl}/me/avatar`, formData).pipe(
      tap(updatedUser => {
        // Met à jour l'état global de l'utilisateur
        this.authService.updateCurrentUser(updatedUser);
      })
    );
  }
}