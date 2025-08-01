import { Component, OnInit } from '@angular/core';
import { finalize, Observable } from 'rxjs';
import { ApplicationService, InstructorApplication } from '../../../core/services/application.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-application-list',
  standalone: false,
  templateUrl: './application-list.component.html',
  styleUrls: ['./application-list.component.scss']
})
export class ApplicationListComponent implements OnInit {
  
  // On n'utilise plus un observable directement, mais un tableau
  applications: InstructorApplication[] = [];
  isLoading = false;
  displayedColumns: string[] = ['name', 'email', 'createdAt', 'actions'];

  constructor(
    private applicationService: ApplicationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadApplications();
  }

  loadApplications(): void {
    this.isLoading = true;
    this.applicationService.getPendingApplications().pipe(
      finalize(() => this.isLoading = false) // S'assure que isLoading passe à false à la fin
    ).subscribe({
      next: (data) => {
        this.applications = data;
      },
      error: (err) => {
        console.error("Erreur lors du chargement des candidatures", err);
        // L'intercepteur d'erreur affiche déjà un message
      }
    });
  }
  
  viewDetails(applicationId: number): void {
    this.router.navigate(['/home/admin-panel/application', applicationId]);
  }
  
}