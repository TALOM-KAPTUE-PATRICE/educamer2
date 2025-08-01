// Interface pour une ressource (PDF, Vidéo, Lien)
export interface Resource {
  id: number;
  name: string;
  type: 'VIDEO' | 'PDF' | 'LINK' | 'IMAGE';
  url: string; // URL publique ou lien interne pour URL signée
  publicId?: string;
   isPrivate?: boolean; // <-- AJOUTER CETTE PROPRIÉTÉ OPTIONNELLE

}

export interface Answer {
  id?: number;
  text: string;
  isCorrect: boolean;
}

export interface Question {
  id?: number;
  text: string;
  answers: Answer[];
}


// Interface pour un quiz lié à une leçon
export interface Quiz {
  id: number;
  title: string;
  questions: Question[]; // <-- Ajouter cette propriété
}




// Interface de base pour un cours (utilisée dans les listes)
export interface Course {
  id: number;
  title: string;
  description: string;
  imageUrl: string | null;
  status: 'DRAFT' | 'PUBLISHED';
  instructorName: string;
  createdAt: string;
}

// Interface complète pour une leçon DANS l'éditeur
export interface Lesson {
  id: number;
  title: string;
  content: string;
  lessonOrder: number;
  resources: Resource[]; // <-- PROPRIÉTÉ AJOUTÉE (corrige l'erreur)
  quiz: Quiz | null;     // <-- PROPRIÉTÉ AJOUTÉE
  completionRequired: boolean; // <-- AJOUTER
  
}

// Interface pour un devoir lié à un cours
export interface Assignment {
  id: number;
  title: string;
  description: string;
  dueDate: string | null;
  attachmentUrl?: string; // <-- AJOUTER CETTE PROPRIÉTÉ OPTIONNELL
    // ▼▼▼ AJOUTS NÉCESSAIRES ▼▼▼
  attachmentIsPrivate?: boolean;
  name?: string; // Le 'name' d'un devoir est son 'title'
}

// Interface pour les données complètes de l'éditeur
export interface CourseEditorData extends Course {
  lessons: Lesson[];
  assignments: Assignment[];
}

// Interface pour les statistiques
export interface CourseStatistic {
  totalEnrollments: number;
  averageProgress: number;
  averageGrade: number;
  pendingSubmissions: number;
}

export interface GradebookEntry {
  submissionId: number;
  assignmentId: number;
  assignmentTitle: string;
  grade: number | null;
  submittedAt: string;
  isGraded: boolean;
}

// Interface pour le carnet de notes d'un élève
export interface StudentGradebook {
  studentId: number;
  studentName: string;
  averageGrade: number;
  grades: GradebookEntry[];
}

// Interface pour la progression d'un élève
export interface StudentProgress {
  studentId: number;
  studentName: string;
  studentAvatarUrl: string;
  courseProgress: number;
  completedLessons: number;
  totalLessons: number;
}