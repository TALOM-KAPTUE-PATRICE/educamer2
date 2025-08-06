import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Course, CourseEditorData, Lesson, Assignment, CourseStatistic, Resource, StudentProgress, StudentGradebook } from '../models/instructor.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class InstructorService {

  private apiUrl = `${environment.apiUrl}/api/instructor`;
  private contentapiUrl = `${environment.apiUrl}/api/instructor/content`; 
  private trackingApiUrl = `${environment.apiUrl}/api/instructor/tracking`;

  constructor(private http: HttpClient) { }

  // ========================================================================
  // ===                 GESTION DE LA LISTE DES COURS                    ===
  // ========================================================================

  /**
   * Récupère la liste de tous les cours créés par l'instructeur authentifié.
   */
  getMyCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.apiUrl}/courses/my-courses`);
  }

  /**
   * Crée un nouveau cours.
   * @param courseData Les données du formulaire de création.
   */
  createCourse(courseData: { title: string, description: string }): Observable<Course> {
    return this.http.post<Course>(`${this.apiUrl}/courses`, courseData);
  }

  // ========================================================================
  // ===                 ÉDITION D'UN COURS SPÉCIFIQUE                    ===
  // ========================================================================

  /**
   * Récupère toutes les données d'un cours nécessaires pour l'éditeur.
   * @param courseId L'ID du cours à éditer.
   */
  getCourseForEditor(courseId: number): Observable<CourseEditorData> {
    return this.http.get<CourseEditorData>(`${this.apiUrl}/courses/${courseId}/editor-details`);
  }

  /**
   * Met à jour les détails de base d'un cours (titre, description).
   * @param courseId L'ID du cours.
   * @param courseDetails Les nouvelles données.
   */
  updateCourseDetails(courseId: number, courseDetails: { title: string, description: string }): Observable<Course> {
    return this.http.put<Course>(`${this.apiUrl}/courses/${courseId}`, courseDetails);
  }

  /**
   * Uploade une nouvelle image de couverture pour un cours.
   * @param courseId L'ID du cours.
   * @param imageFile Le fichier image.
   */
  updateCourseImage(courseId: number, imageFile: File): Observable<Course> {
    const formData = new FormData();
    formData.append('file', imageFile, imageFile.name);
    return this.http.post<Course>(`${this.apiUrl}/courses/${courseId}/image`, formData);
  }

  /**
   * Passe le statut d'un cours à 'PUBLISHED'.
   * @param courseId L'ID du cours à publier.
   */
  publishCourse(courseId: number): Observable<Course> {
    return this.http.post<Course>(`${this.apiUrl}/courses/${courseId}/publish`, {});
  }

  // ========================================================================
  // ===                 GESTION DU CONTENU DU COURS                      ===
  // ========================================================================

  /**
   * Ajoute une nouvelle leçon à un cours.
   * @param courseId L'ID du cours.
   * @param lessonData Les données de la nouvelle leçon.
   */
  addLesson(courseId: number, lessonData: Partial<Lesson>): Observable<Lesson> {
    return this.http.post<Lesson>(`${this.apiUrl}/courses/${courseId}/lessons`, lessonData);
  }

  // ========================================================================
  // ===                        STATISTIQUES                              ===
  // ========================================================================

  /**
   * Récupère les statistiques d'un cours.
   * @param courseId L'ID du cours.
   */
  getCourseStatistics(courseId: number): Observable<CourseStatistic> {
    return this.http.get<CourseStatistic>(`${this.apiUrl}/courses/${courseId}/statistics`);
  }

  /**
 * Met à jour l'ordre des leçons pour un cours.
 * @param courseId L'ID du cours.
 * @param lessonIds La liste des IDs de leçons dans le nouvel ordre.
 */
  updateLessonOrder(courseId: number, lessonIds: number[]): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/courses/${courseId}/lessons/order`, { lessonIds });
  }

  /**
 * Ajoute une nouvelle ressource (fichier ou lien) à une leçon.
 * @param lessonId L'ID de la leçon.
 * @param data Les métadonnées de la ressource (nom, type, url si lien).
 * @param file Le fichier à uploader (optionnel).
 */
  /**
   * Ajoute une nouvelle ressource et retourne la leçon mise à jour.
   */
  addResource(lessonId: number, data: { name: string, type: string, url?: string }, file?: File): Observable<Lesson> {
    const formData = new FormData();
    formData.append('data', new Blob([JSON.stringify(data)], { type: 'application/json' }));

    if (file) {
      formData.append('file', file, file.name);
    }

    // Le type de retour est maintenant Observable<Lesson>
    return this.http.post<Lesson>(`${this.contentapiUrl}/lessons/${lessonId}/resources`, formData);
  }

  deleteResource(resourceId: number): Observable<void> {
    return this.http.delete<void>(`${this.contentapiUrl}/resources/${resourceId}`);
  }

  /**
* Crée ou met à jour le quiz d'une leçon.
* @param lessonId L'ID de la leçon.
* @param quizData Les données du quiz (ex: { title: 'Nouveau titre' }).
* @returns Un observable avec la leçon mise à jour.
*/
  createOrUpdateQuiz(lessonId: number, quizData: { title: string }): Observable<Lesson> {
    return this.http.post<Lesson>(`${this.contentapiUrl}/lessons/${lessonId}/quiz`, quizData);
  }

  addAssignment(courseId: number, data: Partial<Assignment>, file?: File): Observable<Assignment> {
    const formData = new FormData();
    formData.append('data', new Blob([JSON.stringify(data)], { type: 'application/json' }));
    if (file) {
      formData.append('file', file, file.name);
    }
    return this.http.post<Assignment>(`${this.apiUrl}/courses/${courseId}/assignments`, formData);
  }

  updateAssignment(assignmentId: number, data: Partial<Assignment>): Observable<Assignment> {
    return this.http.put<Assignment>(`${this.apiUrl}/courses/assignments/${assignmentId}`, data);
  }

  deleteAssignment(assignmentId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/courses/assignments/${assignmentId}`);
  }


  // ========================================================================
  // ===                 SUIVI DES ÉLÈVES (TRACKING)                    ===
  // ========================================================================

  getCourseProgress(courseId: number): Observable<StudentProgress[]> {
    return this.http.get<StudentProgress[]>(`${this.trackingApiUrl}/course/${courseId}/progress`);
  }

  getCourseGradebook(courseId: number): Observable<StudentGradebook[]> {
    return this.http.get<StudentGradebook[]>(`${this.trackingApiUrl}/course/${courseId}/gradebook`);
  }

  /**
   * Récupère les détails de base d'un cours spécifique.
   * @param courseId L'ID du cours.
  */
  getCourseById(courseId: number): Observable<Course> {
    return this.http.get<Course>(`${this.apiUrl}/courses/${courseId}`);
  }

  deleteCourse(courseId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/courses/${courseId}`);
  }

  deleteLesson(lessonId: number): Observable<void> {
    // L'endpoint est sur le contrôleur de contenu
    return this.http.delete<void>(`${this.contentapiUrl}/lessons/${lessonId}`);
  }
}