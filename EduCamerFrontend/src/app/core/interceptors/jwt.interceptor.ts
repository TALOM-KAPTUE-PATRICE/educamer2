import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../../auth/auth.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = this.authService.getToken();

      const apiUrl = '/api'; // Le proxy s'occupe du reste

    // On vérifie si la requête est destinée à notre API
    const isApiUrl = request.url.startsWith(apiUrl);
    
    // On définit les routes qui NE DOIVENT PAS recevoir le token
    const isPublicRoute = request.url.startsWith('/api/auth') || request.url.startsWith('/api/public');

    if (token && !isPublicRoute && isApiUrl) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    } 
    return next.handle(request);
    
  }
}