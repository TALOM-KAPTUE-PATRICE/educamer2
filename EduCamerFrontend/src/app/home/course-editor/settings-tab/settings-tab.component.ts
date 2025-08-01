import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Course } from '../../../core/models/instructor.model';
import { InstructorService } from '../../../core/services/instructor.service';
import { finalize } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-settings-tab',
  standalone: false,
  templateUrl: './settings-tab.component.html',
  styleUrls: ['./settings-tab.component.scss']
})
export class SettingsTabComponent implements OnInit {
  @Input() course!: Course;
  @Output() courseUpdated = new EventEmitter<void>();
  coverPreviewUrl: string | ArrayBuffer | null = null;
    isUploading = false;
  
  settingsForm!: FormGroup;
  

  constructor(private fb: FormBuilder, private snackBar: MatSnackBar, private instructorService: InstructorService) {}

  ngOnInit(): void {
    this.settingsForm = this.fb.group({
      title: [this.course.title, Validators.required],
      description: [this.course.description, Validators.required]
    });
      this.coverPreviewUrl = this.course.imageUrl; // Initialiser la prévisualisation
  }

  saveSettings(): void {
    if (this.settingsForm.valid && this.settingsForm.dirty) {
      this.instructorService.updateCourseDetails(this.course.id, this.settingsForm.value)
        .subscribe(() => {
          this.courseUpdated.emit(); // Notifier le parent de recharger les données
        });
    }
  }

  onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      // Afficher l'aperçu localement
      const reader = new FileReader();
      reader.onload = () => this.coverPreviewUrl = reader.result;
      reader.readAsDataURL(file);
      
      // Lancer l'upload
      this.isUploading = true;
      this.instructorService.updateCourseImage(this.course.id, file).pipe(
        finalize(() => this.isUploading = false)
      ).subscribe(() => {
        this.snackBar.open('Image mise à jour !', 'OK');
        this.courseUpdated.emit();
      });
    }
  }
  
}