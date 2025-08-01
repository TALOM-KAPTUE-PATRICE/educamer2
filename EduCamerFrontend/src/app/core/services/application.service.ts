import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';


export interface InstructorApplication {
  id: number;
  name: string;
  email: string;
  status: string;
  createdAt: string;
}

export interface InstructorApplicationDetail {
  id: number; name: string; email: string; phone: string;
  specializations: string; motivation: string; resumeUrl: string; encodedResumePublicId:string;
}


@Injectable({
  providedIn: 'root'
})
export class ApplicationService {
  private publicApiUrl = '/api/public/instructor-applications';
  private adminApiUrl = '/api/admin/applications';

  constructor(private http: HttpClient) { }

  // --- Pour le candidat ---
  submitApplication(formData: FormData): Observable<string> {
    return this.http.post(this.publicApiUrl, formData, { responseType: 'text' });
  }

  // --- Pour l'admin ---
  getPendingApplications(): Observable<any[]> { // Remplacer 'any' par une interface InstructorApplicationDTO
    return this.http.get<any[]>(this.adminApiUrl);
  }

  approveApplication(applicationId: number): Observable<string> {
    return this.http.post(`${this.adminApiUrl}/${applicationId}/approve`, {}, { responseType: 'text' });
  }

  rejectApplication(applicationId: number, reason: { reason: string }): Observable<string> {
    return this.http.post(`${this.adminApiUrl}/${applicationId}/reject`, reason, { responseType: 'text' });
  }

    /**
   * Récupère les détails complets d'une candidature spécifique.
   * @param applicationId L'ID de la candidature.
   */
  getApplicationById(applicationId: number): Observable<InstructorApplicationDetail> {
    return this.http.get<InstructorApplicationDetail>(`${this.adminApiUrl}/${applicationId}`);
  }
}