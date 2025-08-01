import { Component, EventEmitter, Output } from '@angular/core';
import { Observable } from 'rxjs';
import {  UserService } from '../../../core/services/user.service';
import { AuthService, CurrentUser } from '../../../auth/auth.service';

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  @Output() toggleSidebar = new EventEmitter<void>();
  currentUser$: Observable<CurrentUser | null>;

  // On injecte AuthService qui contient la logique
  constructor(public authService: AuthService) {
    this.currentUser$ = this.authService.currentUser$;
  }


  onToggleSidebar() {
    this.toggleSidebar.emit();
  }
}