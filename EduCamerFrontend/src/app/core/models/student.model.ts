import { Assignment, Course, Lesson, Quiz, Resource } from './instructor.model'; // On peut réutiliser certains modèles

// Interface pour un cours affiché dans le catalogue
export interface CatalogCourse extends Course {
  averageRating: number;
  enrolledStudentCount: number;
  lessonCount: number;
}

export interface Category {
  id: number;
  name: string;
}

export interface LinkingCodeResponse {
  code: string;
  expiryMessage: string;
}

// Interface pour les détails d'un cours auquel l'élève est inscrit
export interface EnrolledCourseDetails {
  id: number;
  title: string;
  description: string;
  instructorName: string;
  lessons: LessonSummary[]; // Utilise un résumé de la leçon
  assignments: Assignment[]; 
}

// Interface pour un résumé de leçon dans la sidebar du cours
export interface LessonSummary {
  id: number;
  title: string;
  lessonOrder: number;
  isAccessible?: boolean;
  completionRequired: boolean; // <-- PROPRIÉTÉ MANQUANTE À AJOUTER
  isCompleted?: boolean; // Pour afficher une icône de validation
}

// Interface pour les détails complets d'une leçon
// Elle hérite de Lesson (qui a déjà content, resources, quiz)
// ET nous allons nous assurer qu'elle a aussi les propriétés de LessonSummary
export interface LessonDetails extends Lesson {
  isCompleted?: boolean; // <-- AJOUTER
  isAccessible?: boolean; // <-- AJOUTER
}

// ▼▼▼ NOUVELLE INTERFACE À AJOUTER ▼▼▼
// Étend l'interface de base pour inclure les réponses correctes
export interface QuizAttemptResultReview extends QuizAttemptResult {
  correctAnswers: { [key: number]: number }; // Clé: questionId, Valeur: correctAnswerId
}

// NOUVEAU: Interface pour la page "Mes Cours"
export interface EnrolledCourseSummary extends Course {
  progress?: number; // Pourcentage de progression
}

export interface HelpRequest {
  id: number;
  subject: string;
  description: string;
  status: string;
  studentId: number;
  studentName: string;
  createdAt: string;
}

export interface QuizAttemptResult {
  attemptId: number;
  score: number;
  totalQuestions: number;
  percentage: number;
  message: string;
}