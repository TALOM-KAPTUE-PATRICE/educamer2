import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LinkedStudent } from '../models/parent.model'; // Modèle à créer
import { StudentProgress, StudentGradebook } from '../models/instructor.model';
import { EnrolledCourseSummary } from '../models/student.model';

@Injectable({ providedIn: 'root' })
export class ParentService {
  private apiUrl = '/api/parent';

  constructor(private http: HttpClient) {}

  /**
   * Envoie la demande de liaison de compte au backend.
   * @param payload Un objet contenant studentEmail et linkingCode.
   */
  linkStudent(payload: { studentEmail: string, linkingCode: string }): Observable<LinkedStudent> {
    // On envoie l'objet 'payload' complet, qui correspond au DTO LinkStudentRequest du backend.
    return this.http.post<LinkedStudent>(`${this.apiUrl}/link-student`, payload);
  }


  getLinkedStudents(): Observable<LinkedStudent[]> {
    return this.http.get<LinkedStudent[]>(`${this.apiUrl}/my-children`);
  }

  getChildProgress(studentId: number, courseId: number): Observable<StudentProgress> {
    return this.http.get<StudentProgress>(`${this.apiUrl}/children/${studentId}/tracking/course/${courseId}/progress`);
  }

  getChildGradebook(studentId: number, courseId: number): Observable<StudentGradebook> {
    return this.http.get<StudentGradebook>(`${this.apiUrl}/children/${studentId}/tracking/course/${courseId}/gradebook`);
  }

    /**
     * Récupère les cours auxquels un enfant spécifique est inscrit.
     */
    getEnrolledCoursesForChild(studentId: number): Observable<EnrolledCourseSummary[]> {
        return this.http.get<EnrolledCourseSummary[]>(`${this.apiUrl}/children/${studentId}/courses`);
    }

    
  
}