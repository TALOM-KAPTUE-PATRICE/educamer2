import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StudentGradebook } from '../models/instructor.model'; // Réutiliser le modèle
import { CatalogCourse, Category, EnrolledCourseDetails, EnrolledCourseSummary, LessonDetails, QuizAttemptResult, QuizAttemptResultReview } from '../models/student.model';
import { HelpRequest } from '../models/student.model';
import { LinkingCodeResponse } from '../models/student.model';
import { environment } from '../../../environments/environment';


@Injectable({
  providedIn: 'root'
})

export class StudentService {

  private apiUrl = `${environment.apiUrl}/catalog`; 
  private studentApiUrl = `${environment.apiUrl}/student`;
  private quizApiUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) { }

  getTrendingCourses(limit: number = 4): Observable<CatalogCourse[]> {
    return this.http.get<CatalogCourse[]>(`${this.apiUrl}/courses/trending`, { params: { limit } });
  }

  getTopRatedCourses(limit: number = 4): Observable<CatalogCourse[]> {
    return this.http.get<CatalogCourse[]>(`${this.apiUrl}/courses/top-rated`);
  }

  getEnrolledCourseDetails(courseId: number): Observable<EnrolledCourseDetails> {
    return this.http.get<EnrolledCourseDetails>(`${this.studentApiUrl}/courses/${courseId}/details`);
  }

  getLessonDetails(courseId: number, lessonId: number): Observable<LessonDetails> {
    return this.http.get<LessonDetails>(`${this.studentApiUrl}/courses/${courseId}/lessons/${lessonId}`);
  }

  getAllCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.apiUrl}/categories`);
  }

  getCoursesByCategory(categoryId: number): Observable<CatalogCourse[]> {
    return this.http.get<CatalogCourse[]>(`${this.apiUrl}/categories/${categoryId}/courses`);
  }

  getAllCourses(): Observable<CatalogCourse[]> {
    return this.http.get<CatalogCourse[]>(`${this.apiUrl}/courses`);
  }

  enrollInCourse(courseId: number): Observable<void> {
    return this.http.post<void>(`${this.studentApiUrl}/courses/${courseId}/enroll`, {});
  }

  getMyEnrolledCourses(): Observable<EnrolledCourseSummary[]> { // Remplacer 'any' par un modèle fort
    return this.http.get<EnrolledCourseSummary[]>(`${this.studentApiUrl}/my-courses`);
  }

  /**
 * Récupère le carnet de notes d'un élève pour un cours spécifique.
 */
  getMyGrades(courseId: number): Observable<StudentGradebook> {
    // Note: L'endpoint doit retourner le StudentGradebookDTO d'UN SEUL élève (celui authentifié)
    return this.http.get<StudentGradebook>(`${this.studentApiUrl}/tracking/course/${courseId}/my-grades`);
  }

  /**
   * Crée une nouvelle demande d'assistance.
   */
  createHelpRequest(requestData: { subject: string, description: string }): Observable<HelpRequest> {
    return this.http.post<HelpRequest>(`${this.studentApiUrl}/help-requests`, requestData);
  }

  /**
   * Soumet les réponses d'un quiz et retourne le résultat complet avec la correction.
   */
  submitQuiz(quizId: number, attemptData: { answers: { [key: number]: number } }): Observable<QuizAttemptResultReview> {
    // On type la requête HTTP pour qu'elle attende le bon objet en réponse
    return this.http.post<QuizAttemptResultReview>(`${this.quizApiUrl}/quizzes/${quizId}/attempt`, attemptData);
  }


  /**
   * Demande au backend de générer un nouveau code de liaison pour l'élève authentifié.
  */

  generateLinkingCode(): Observable<LinkingCodeResponse> {
    return this.http.post<LinkingCodeResponse>(`${this.studentApiUrl}/generate-linking-code`, {});
  }


}