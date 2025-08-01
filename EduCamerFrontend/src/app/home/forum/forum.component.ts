import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { ForumThread } from '../../core/models/forum.model';
import { ForumService } from '../../core/services/forum.service';
import { MatDialog } from '@angular/material/dialog';
import { CreateThreadDialogComponent } from './dialogs/create-thread-dialog/create-thread-dialog.component';

@Component({
  selector: 'app-forum',
  standalone: false,
  templateUrl: './forum.component.html',
  styleUrl: './forum.component.scss'
})
export class ForumComponent implements OnInit{


  threads$!: Observable<ForumThread[]>;
  courseId!: number;
  snackBar: any;

  constructor(
    private route: ActivatedRoute,
    private forumService: ForumService,
    private dialog : MatDialog
  ) {}

  ngOnInit(): void {
    // Récupérer l'ID du cours depuis l'URL
    this.courseId = Number(this.route.snapshot.paramMap.get('courseId'));
    if (this.courseId) {
      this.threads$ = this.forumService.getThreadsForCourse(this.courseId);
    }
  }

  openCreateThreadDialog(): void {
    const dialogRef = this.dialog.open(CreateThreadDialogComponent, { width: '600px' });
    
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.forumService.createThread(this.courseId, result).subscribe({
          next: () => {
            this.snackBar.open('Discussion créée !', 'OK');
            this.threads$ = this.forumService.getThreadsForCourse(this.courseId); // Recharger
          },
          error: () => this.snackBar.open('Erreur lors de la création.', 'OK')
        });
      }
    });
  }

}
