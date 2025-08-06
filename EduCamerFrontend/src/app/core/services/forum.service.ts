import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
// Importer les modèles de Forum
import { ForumThread, ForumPost } from '../models/forum.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ForumService {


  private forumApiUrl = `${environment.apiUrl}/api/forum`;
  private moderationApiUrl = `${environment.apiUrl}/api/moderation/forum`;

  constructor(private http: HttpClient) { }

  getThreadsForCourse(courseId: number): Observable<ForumThread[]> {
    return this.http.get<ForumThread[]>(`${this.forumApiUrl}/course/${courseId}/threads`);
  }

  getThreadById(threadId: number): Observable<ForumThread> {
    // Il faudra créer cet endpoint backend
    return this.http.get<ForumThread>(`${this.forumApiUrl}/threads/${threadId}`);
  }

  addPost(threadId: number, content: string): Observable<ForumPost> {
    return this.http.post<ForumPost>(`${this.forumApiUrl}/threads/${threadId}/posts`, { content });
  }

  // --- Actions de Modération ---
  deletePost(postId: number): Observable<void> {
    return this.http.delete<void>(`${this.moderationApiUrl}/posts/${postId}`);
  }

  /**
    * Met à jour le contenu d'un message existant (action de modération).
    * @param postId L'ID du message à modifier.
    * @param newContent Le nouveau contenu du message.
    */
  updatePost(postId: number, newContent: string): Observable<ForumPost> {
    return this.http.put<ForumPost>(`${this.moderationApiUrl}/posts/${postId}`, { content: newContent });
  }

  createThread(courseId: number, data: { title: string, firstPostContent: string }): Observable<ForumThread> {
    return this.http.post<ForumThread>(`${this.forumApiUrl}/course/${courseId}/threads`, data);
  }
}