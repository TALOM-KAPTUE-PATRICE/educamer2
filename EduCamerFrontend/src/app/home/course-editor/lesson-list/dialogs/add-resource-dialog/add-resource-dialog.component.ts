import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Resource } from '../../../../../core/models/instructor.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { trigger, transition, style, animate } from '@angular/animations';


// Définir les contraintes de manière centralisée
const FILE_CONSTRAINTS = {
  PDF: {
    types: ['application/pdf'],
    maxSizeMB: 10
  },
  VIDEO: {
    types: ['video/mp4', 'video/quicktime', 'video/webm'],
    maxSizeMB: 100 // Augmenter la limite pour les vidéos
  },
  IMAGE: {
    types: ['image/jpeg', 'image/png', 'image/gif'],
    maxSizeMB: 5
  }
};



@Component({
  selector: 'app-add-resource-dialog',
  standalone: false,
  templateUrl: './add-resource-dialog.component.html',
  styleUrls: ['./add-resource-dialog.component.scss'],
  animations: [
    trigger('fadeInOut', [
      transition(':enter', [style({ opacity: 0, transform: 'translateY(-10px)' }), animate('300ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))]),
      transition(':leave', [animate('300ms ease-in', style({ opacity: 0, transform: 'translateY(-10px)' }))])
    ])
  ]
})
export class AddResourceDialogComponent implements OnInit {
  resourceForm!: FormGroup;
  selectedFile: File | null = null;
  isDragOver = false;
    // Référence à l'input de fichier pour le réinitialiser
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;


  
  constructor(
    private fb: FormBuilder,
     private snackBar: MatSnackBar,
    public dialogRef: MatDialogRef<AddResourceDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { lessonId: number }
  ) {}

  ngOnInit(): void {
    this.resourceForm = this.fb.group({
      type: ['', Validators.required],
      name: ['', Validators.required],
      url: ['']
    });

    this.resourceForm.get('type')?.valueChanges.subscribe(type => {
      const urlControl = this.resourceForm.get('url');
      if (type === 'LINK') {
        urlControl?.setValidators([Validators.required, Validators.pattern('https?://.+')]);
        this.removeFile(new Event('change')); // Si on passe à LINK, on retire le fichier sélectionné
      } else {
        urlControl?.clearValidators();
      }
      urlControl?.updateValueAndValidity();
    });
  }
    // --- Gestion du Drag and Drop ---
  onDragOver(event: DragEvent) { event.preventDefault(); this.isDragOver = true; }
  onDragLeave(event: DragEvent) { event.preventDefault(); this.isDragOver = false; }
  

   onDrop(event: DragEvent) {
    event.preventDefault();
    this.isDragOver = false;
    if (event.dataTransfer?.files && event.dataTransfer.files.length > 0) {
      this.handleFile(event.dataTransfer.files[0]);
      event.dataTransfer.clearData();
    }
  }

  onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) this.handleFile(file);
  }

  /**
   * Méthode centrale pour valider un fichier (taille et type).
   */
  handleFile(file: File): void {
    const type = this.resourceForm.get('type')?.value;
    if (!type || type === 'LINK') {
      this.snackBar.open('Veuillez d\'abord sélectionner un type de fichier (PDF, Vidéo...).', 'OK');
      this.resetFileInput();
      return;
    }

    const constraints = FILE_CONSTRAINTS[type as keyof typeof FILE_CONSTRAINTS];
    
    // 1. Vérifier le type MIME
    if (!constraints.types.includes(file.type)) {
      this.snackBar.open(`Type de fichier invalide. Attendu: ${constraints.types.join(', ')}`, 'OK', { duration: 5000 });
      this.resetFileInput();
      return;
    }

    // 2. Vérifier la taille
    if (file.size > constraints.maxSizeMB * 1024 * 1024) {
      this.snackBar.open(`Fichier trop volumineux. Maximum autorisé: ${constraints.maxSizeMB}MB.`, 'OK', { duration: 5000 });
      this.resetFileInput();
      return;
    }

    // Si tout est bon, on accepte le fichier
    this.selectedFile = file;
  }

  removeFile(event: Event): void {
    event.stopPropagation();
    this.selectedFile = null;
  }

  private resetFileInput(): void {
    if (this.fileInput) {
      this.fileInput.nativeElement.value = '';
    }
  }


  isFormValid(): boolean {
    if (!this.resourceForm.valid) return false;
    const type = this.resourceForm.get('type')?.value;
    if (type !== 'LINK' && !this.selectedFile) return false;
    return true;
  }
  
  
  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    if (this.isFormValid()) {
      this.dialogRef.close({
        data: this.resourceForm.value,
        file: this.selectedFile
      });
    }
  }
}