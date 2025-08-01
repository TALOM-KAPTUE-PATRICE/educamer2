import { Component, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-dashboard-layout',
  standalone: false,
  templateUrl: './dashboard-layout.component.html',
  styleUrls: ['./dashboard-layout.component.scss']
})
export class DashboardLayoutComponent {
  @ViewChild('drawer') drawer!: MatSidenav;

  // 1. Déclarez la propriété sans l'initialiser ici
  isHandset$: Observable<boolean>;

  constructor(private breakpointObserver: BreakpointObserver, public authService: AuthService) {
    // 'breakpointObserver' est maintenant disponible, on peut l'utiliser
    this.isHandset$ = this.breakpointObserver.observe(Breakpoints.Handset)
      .pipe(
        map(result => result.matches),
        shareReplay()
      );

  }

  toggleSidebar() {
    this.drawer.toggle();
  }
}