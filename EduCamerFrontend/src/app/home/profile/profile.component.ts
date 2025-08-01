import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../core/services/user.service';
import { AuthService } from '../../auth/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { finalize } from 'rxjs/operators';
import { StudentService } from '../../core/services/student.service';

@Component({
  selector: 'app-profile',
  standalone: false,
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  profileForm!: FormGroup;
  isSavingInfo = false;
  isUploadingAvatar = false;

    // NOUVEAU: Propriétés pour la liaison
  linkingCode: string | null = null;
  expiryMessage: string | null = null;
  isGenerating = false;
  
  selectedFile: File | null = null;
  avatarPreviewUrl: string | ArrayBuffer | null = null;

  constructor(
    public authService: AuthService,
    private fb: FormBuilder,
    private userService: UserService,
    private snackBar: MatSnackBar,
    private studentService: StudentService, // Injecter
  ) {}

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      name: ['', Validators.required],
      email: [{ value: '', disabled: true }]
    });

    this.authService.currentUser$.subscribe(user => {
      if (user) {
        this.profileForm.patchValue(user);
        this.avatarPreviewUrl = user.avatarUrl || null;
      }
    });
  }

  onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      if (file.size > 2 * 1024 * 1024) { // Limite de 2MB
        this.snackBar.open('Le fichier est trop lourd. (Max 2MB)', 'Fermer', { panelClass: 'error-snackbar' });
        return;
      }
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = () => this.avatarPreviewUrl = reader.result;
      reader.readAsDataURL(file);
    }
  }

  uploadAvatar(): void {
    if (!this.selectedFile) return;

    this.isUploadingAvatar = true;
    this.userService.updateAvatar(this.selectedFile).pipe(
      finalize(() => this.isUploadingAvatar = false)
    ).subscribe({
      next: () => {
        this.snackBar.open('Avatar mis à jour avec succès !', 'OK', { duration: 3000 });
        this.selectedFile = null;
      },
      error: () => {} // L'intercepteur gère le message
    });
  }
  
  cancelAvatarChange(): void {
    this.selectedFile = null;
    // On utilise maintenant le getter public au lieu d'accéder directement à la propriété privée
    const currentUser = this.authService.currentUserValue; // <-- CORRECTION ICI
    this.avatarPreviewUrl = currentUser?.avatarUrl || null;
  }


  onSubmitInfo(): void {
    if (this.profileForm.invalid || !this.profileForm.dirty) return;

    this.isSavingInfo = true;
    this.userService.updateProfile({ name: this.profileForm.value.name }).pipe(
      finalize(() => this.isSavingInfo = false)
    ).subscribe({
      next: () => {
        this.snackBar.open('Profil mis à jour avec succès !', 'OK', { duration: 3000 });
        this.profileForm.markAsPristine();
      },
      error: () => {}
    });
  }

    // NOUVEAU: Méthode pour générer le code
  generateCode(): void {
    this.isGenerating = true;
    this.studentService.generateLinkingCode().subscribe({
      next: (response) => {
        this.linkingCode = response.code;
        this.expiryMessage = response.expiryMessage;
        this.snackBar.open('Code généré ! Partagez-le rapidement avec votre parent.', 'OK', { duration: 5000 });
        this.isGenerating = false;
      },
      error: () => {
        this.snackBar.open('Erreur lors de la génération du code.', 'OK');
        this.isGenerating = false;
      }
    });
  }
}