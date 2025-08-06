import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ChatbotService {
    private apiUrl = `${environment.apiUrl}/chatbot`; 

  constructor(private http: HttpClient) {}

  ask(question: string): Observable<{ answer: string }> {
    return this.http.post<{ answer: string }>(`${this.apiUrl}/ask`, { question });
  }
}