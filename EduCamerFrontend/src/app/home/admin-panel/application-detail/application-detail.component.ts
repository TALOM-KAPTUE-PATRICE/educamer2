import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ApplicationService, InstructorApplicationDetail } from '../../../core/services/application.service';
import { finalize, Observable } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { RejectionDialogComponent } from '../../../dialog/rejection-dialog/rejection-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';



@Component({
  selector: 'app-application-detail',
  standalone: false,
  templateUrl: './application-detail.component.html',
  styleUrls: ['./application-detail.component.scss']
})
export class ApplicationDetailComponent implements OnInit {
  application: InstructorApplicationDetail | null = null;
  isLoading = true; // Commence en chargement
  isProcessingAction = false; // Pour les boutons Approuver/Rejeter
  applicationId!: number;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private applicationService: ApplicationService,
    private snackBar: MatSnackBar,
    public dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.applicationId = +this.route.snapshot.paramMap.get('id')!;
    if (this.applicationId) {
      this.loadApplicationDetails();
    } else {
      this.isLoading = false; // Pas d'ID, pas de chargement
    }
  }

  loadApplicationDetails(): void {
    this.isLoading = true;
    this.applicationService.getApplicationById(this.applicationId).pipe(
      finalize(() => this.isLoading = false)
    ).subscribe({
      next: (data) => {
        this.application = data;
      },
      error: () => {
        // L'intercepteur gère le message. On redirige.
        this.router.navigate(['/home/admin-panel']);
      }
    });
  }

  approve(): void {
    this.isProcessingAction = true;
    this.applicationService.approveApplication(this.applicationId).pipe(
      finalize(() => this.isProcessingAction = false)
    ).subscribe({
      next: (message) => {
        this.snackBar.open(message, 'OK', { duration: 5000 });
        this.router.navigate(['/home/admin-panel']);
      }
    });
  }

  openRejectDialog(): void {
    const dialogRef = this.dialog.open(RejectionDialogComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (result && result.reason) {
        this.isProcessingAction = true;
        this.applicationService.rejectApplication(this.applicationId, { reason: result.reason }).pipe(
          finalize(() => this.isProcessingAction = false)
        ).subscribe({
          next: (message) => {
            this.snackBar.open(message, 'OK', { duration: 5000 });
            this.router.navigate(['/home/admin-panel']);
          }
        });
      }
    });
  }
}