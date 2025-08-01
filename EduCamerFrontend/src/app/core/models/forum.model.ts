export interface ForumPost {
  id: number;
  content: string;
  authorId: number;
  authorName: string;
  authorAvatarUrl: string | null;
  createdAt: string;
}

// Interface pour un fil de discussion (sujet)
export interface ForumThread {
  id: number;
  title: string;
  authorId: number;
  authorName: string;
  createdAt: string;
  courseId: number; // Important pour la navigation retour
  posts: ForumPost[];
}