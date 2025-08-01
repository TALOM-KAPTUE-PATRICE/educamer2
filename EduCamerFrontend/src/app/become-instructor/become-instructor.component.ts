import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { StepperOrientation } from '@angular/material/stepper';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ApplicationService } from '../core/services/application.service';
import { MatDialog } from '@angular/material/dialog';
import { ApplicationReviewDialogComponent, ApplicationReviewData } from '../dialogs/application-review-dialog/application-review-dialog.component';

@Component({
  selector: 'app-become-instructor',
  standalone: false,  
  templateUrl: './become-instructor.component.html',
  styleUrls: ['./become-instructor.component.scss']
})
export class BecomeInstructorComponent implements OnInit {
  firstFormGroup!: FormGroup;
  secondFormGroup!: FormGroup;
  selectedFile: File | null = null;
  fileName = '';
  isSubmitting = false;
  
  stepperOrientation: Observable<StepperOrientation>;

  constructor(
    private fb: FormBuilder,
    private breakpointObserver: BreakpointObserver,
    private applicationService: ApplicationService,
    private snackBar: MatSnackBar,
    private router: Router,
    private dialog: MatDialog // Injecter MatDialog
  ) {
    this.stepperOrientation = this.breakpointObserver
      .observe([Breakpoints.Small, Breakpoints.XSmall])
      .pipe(map(({ matches }) => (matches ? 'vertical' : 'horizontal')));
  }

  ngOnInit(): void {
    this.firstFormGroup = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      desiredRole: ['', Validators.required] 
    });

    this.secondFormGroup = this.fb.group({
      specializations: ['', Validators.required],
      motivation: ['', [Validators.required, Validators.minLength(50)]],
      resume: [null, Validators.required]
    });
  }

  onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      this.selectedFile = file;
      this.fileName = file.name;
      this.secondFormGroup.patchValue({ resume: file });
      this.secondFormGroup.get('resume')?.updateValueAndValidity();
    }
  }

  // NOUVELLE MÉTHODE: Ouvre la dialogue de confirmation
  openReviewDialog(): void {
    if (this.firstFormGroup.invalid || this.secondFormGroup.invalid) {
      // Marquer tous les champs comme "touchés" pour afficher les erreurs
      this.firstFormGroup.markAllAsTouched();
      this.secondFormGroup.markAllAsTouched();
      this.snackBar.open('Veuillez remplir tous les champs requis avant de continuer.', 'OK');
      return;
    }

    const reviewData: ApplicationReviewData = {
      personalInfo: this.firstFormGroup.value,
      expertiseInfo: this.secondFormGroup.value,
      fileName: this.fileName,
      parentComponent: this 
    };

    const dialogRef = this.dialog.open(ApplicationReviewDialogComponent, {
      width: '700px',
      data: reviewData,
      disableClose: true // Empêche de fermer pendant la soumission
    });

    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.submitApplication(); // Si l'utilisateur confirme, on envoie
      }
    });
  }

  // Renommée pour plus de clarté
  private submitApplication(): void {  
    this.isSubmitting = true;
    const formData = new FormData();
    
    // Ajouter les données des deux formulaires
    Object.keys(this.firstFormGroup.value).forEach(key => {
      formData.append(key, this.firstFormGroup.value[key]);
    });
    Object.keys(this.secondFormGroup.value).forEach(key => {
      if (key !== 'resume') { // Ne pas ajouter le contrôle de fichier
        formData.append(key, this.secondFormGroup.value[key]);
      }
    });

    if (this.selectedFile) {
      formData.append('resume', this.selectedFile, this.selectedFile.name);
    }

    this.applicationService.submitApplication(formData).subscribe({
      next: (response: string) => {
        this.isSubmitting = false;
        this.snackBar.open(response, 'Génial !', { duration: 10000, panelClass: 'success-snackbar' });
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.isSubmitting = false;
        console.error("Erreur lors de la soumission:", err);
      }
    });
  }
}