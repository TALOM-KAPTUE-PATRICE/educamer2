import { Component, Input } from '@angular/core';
import { LinkedStudent } from '../../../core/models/parent.model';
import { MatDialog } from '@angular/material/dialog';
import { ChildGradesDialogComponent } from '../dialogs/child-grades-dialog/child-grades-dialog.component';
import { ChildProgressDialogComponent } from '../dialogs/child-progress-dialog/child-progress-dialog.component';
// Importer les futurs composants de dialogue
// import { ChildProgressDialogComponent } from '../dialogs/child-progress-dialog.component';
// import { ChildGradesDialogComponent } from '../dialogs/child-grades-dialog.component';


@Component({
  selector: 'app-child-card',
  standalone: false,
  templateUrl: './child-card.component.html',
  styleUrl: './child-card.component.scss'
})
export class ChildCardComponent {

  // ▼▼▼ CORRECTION : On déclare que le composant accepte un 'student' ▼▼▼
  @Input() student!: LinkedStudent;


  constructor(private dialog: MatDialog) { }
  
  viewProgress(): void {
    this.dialog.open(ChildProgressDialogComponent, {
      width: '800px',
      data: { studentId: this.student.id, studentName: this.student.name }
    });
  }

  viewGrades(): void {
    this.dialog.open(ChildGradesDialogComponent, {
      width: '900px',
      data: { studentId: this.student.id, studentName: this.student.name }
    });
  }
}
