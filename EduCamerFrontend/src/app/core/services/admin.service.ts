import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

// Interface pour représenter un utilisateur dans la liste
export interface ManagedUser {
  id: number;
  name: string;
  email: string;
  role: string;
  avatarUrl?: string;
  enabled: boolean; // <-- AJOUTER
}

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = `${environment.apiUrl}/admin/users`; 

  constructor(private http: HttpClient) { }

  /**
   * Récupère la liste de tous les utilisateurs de la plateforme.
   */
  getAllUsers(): Observable<ManagedUser[]> {
    return this.http.get<ManagedUser[]>(this.apiUrl);
  }

  /**
   * Supprime un utilisateur par son ID.
   * @param userId L'ID de l'utilisateur à supprimer.
   */
  deleteUser(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${userId}`);
  }
  
  /**
   * Désactive (suspend) un compte utilisateur.
   */
  disableUser(userId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${userId}/disable`, {});
  }

  /**
   * Active un compte utilisateur.
   */
  enableUser(userId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${userId}/enable`, {});
  }
}