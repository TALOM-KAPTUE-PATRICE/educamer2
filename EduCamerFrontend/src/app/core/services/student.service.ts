import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StudentGradebook } from '../models/instructor.model'; // Réutiliser le modèle
import { CatalogCourse, Category, EnrolledCourseDetails, EnrolledCourseSummary, LessonDetails, QuizAttemptResult, QuizAttemptResultReview } from '../models/student.model';
import { HelpRequest } from '../models/student.model';
import { LinkingCodeResponse } from '../models/student.model';


@Injectable({  
  providedIn: 'root'
})

export class StudentService {
  private apiUrl = '/api/catalog'; // L'API du catalogue

  private studentApiUrl = '/api/student';
  constructor(private http: HttpClient) {}

  getTrendingCourses(limit: number = 4): Observable<CatalogCourse[]> {
    return this.http.get<CatalogCourse[]>(`${this.apiUrl}/courses/trending`, { params: { limit } });
  }
  
  getTopRatedCourses(limit: number = 4): Observable<CatalogCourse[]> {
    return this.http.get<CatalogCourse[]>(`${this.apiUrl}/courses/top-rated`);
  }

  getEnrolledCourseDetails(courseId: number): Observable<EnrolledCourseDetails> {
    return this.http.get<EnrolledCourseDetails>(`/api/student/courses/${courseId}/details`);
  }

  getLessonDetails(courseId: number, lessonId: number): Observable<LessonDetails> {
    return this.http.get<LessonDetails>(`/api/student/courses/${courseId}/lessons/${lessonId}`);
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
    return this.http.post<void>(`/api/student/courses/${courseId}/enroll`, {});
  }

  getMyEnrolledCourses(): Observable<EnrolledCourseSummary[]> { // Remplacer 'any' par un modèle fort
    return this.http.get<EnrolledCourseSummary[]>('/api/student/my-courses');
  }

    /**
   * Récupère le carnet de notes d'un élève pour un cours spécifique.
   */
  getMyGrades(courseId: number): Observable<StudentGradebook> {
    // Note: L'endpoint doit retourner le StudentGradebookDTO d'UN SEUL élève (celui authentifié)
    return this.http.get<StudentGradebook>(`/api/student/tracking/course/${courseId}/my-grades`);
  }

  /**
   * Crée une nouvelle demande d'assistance.
   */
  createHelpRequest(requestData: { subject: string, description: string }): Observable<HelpRequest> {
    return this.http.post<HelpRequest>('/api/student/help-requests', requestData);
  }
  
  /**
   * Soumet les réponses d'un quiz et retourne le résultat complet avec la correction.
   */
  submitQuiz(quizId: number, attemptData: { answers: { [key: number]: number } }): Observable<QuizAttemptResultReview> {
    // On type la requête HTTP pour qu'elle attende le bon objet en réponse
    return this.http.post<QuizAttemptResultReview>(`/api/quizzes/${quizId}/attempt`, attemptData);
  }


    /**
     * Demande au backend de générer un nouveau code de liaison pour l'élève authentifié.
    */
   
    generateLinkingCode(): Observable<LinkingCodeResponse> {
        return this.http.post<LinkingCodeResponse>(`${this.studentApiUrl}/generate-linking-code`, {});
    }

 
}