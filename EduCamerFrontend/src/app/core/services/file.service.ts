import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  constructor(private http: HttpClient) {}

  /**
   * Télécharge un fichier sécurisé.
   * L'API backend est censée renvoyer le contenu du fichier (blob).
   * @param apiUrl L'URL interne de notre API (ex: /api/files/resource/123/access)
   * @param fileName Le nom à donner au fichier téléchargé.
   */
  downloadSecureFile(apiUrl: string, fileName: string): void {
    // On fait un appel GET qui attend un 'blob' (un fichier binaire) en réponse
    this.http.get(apiUrl, { responseType: 'blob' }).subscribe(blob => {
      // Créer un lien en mémoire
      const a = document.createElement('a');
      const objectUrl = URL.createObjectURL(blob);
      a.href = objectUrl;
      a.download = fileName; // Donner un nom au fichier
      a.click();
      
      // Nettoyer l'URL en mémoire après le clic
      URL.revokeObjectURL(objectUrl);
    });
  }
}