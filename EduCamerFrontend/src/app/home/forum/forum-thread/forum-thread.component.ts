import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { AuthService } from '../../../auth/auth.service';
import { ForumService } from '../../../core/services/forum.service';
import { ConfirmDialogComponent } from '../../../dialogs/confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ForumThread, ForumPost } from '../../../core/models/forum.model';
import { EditPostDialogComponent } from '../dialogs/edit-post-dialog/edit-post-dialog.component';

@Component({
  selector: 'app-forum-thread',
  standalone: false,
  templateUrl: './forum-thread.component.html',
  styleUrl: './forum-thread.component.scss'
})
export class ForumThreadComponent implements OnInit {
  threadId!: number;
  courseId!: number; 
  thread$!: Observable<ForumThread>;
  replyForm!: FormGroup;

  constructor(
    public authService: AuthService,
    private route: ActivatedRoute,
    private forumService: ForumService,
    private fb: FormBuilder,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    
    this.threadId = Number(this.route.snapshot.paramMap.get('threadId'));
    // On charge le thread et on stocke son courseId
    this.thread$ = this.forumService.getThreadById(this.threadId).pipe(
      tap(thread => {
        if (thread) {
          this.courseId = thread.courseId;
        }
      })
    );
  
    this.replyForm = this.fb.group({
      content: ['', [Validators.required, Validators.minLength(5)]]
    });
  }

  loadThread(): void {
    this.thread$ = this.forumService.getThreadById(this.threadId);
  }

  onReply(): void {
    if (this.replyForm.invalid) return;

    const content = this.replyForm.value.content;
    this.forumService.addPost(this.threadId, content).subscribe({
      next: () => {
        this.snackBar.open('Réponse publiée !', 'OK', { duration: 3000 });
        this.replyForm.reset();
        this.loadThread(); // Recharger le fil de discussion pour voir le nouveau message
      },
      error: () => this.snackBar.open('Erreur lors de la publication.', 'OK')
    });
  }

  editPost(post: ForumPost): void {
    const dialogRef = this.dialog.open(EditPostDialogComponent, {
      width: '600px',
      data: { content: post.content }
    });

    dialogRef.afterClosed().subscribe(newContent => {
      if (newContent) {
        // On appelle la nouvelle méthode du service
        this.forumService.updatePost(post.id, newContent).subscribe({
          next: () => {
            this.snackBar.open('Message modifié avec succès.', 'OK', { duration: 3000 });
            this.loadThread(); // Recharger pour voir la modification
          },
          error: () => this.snackBar.open('Erreur lors de la modification.', 'OK')
        });
      }
    });
  }

  deletePost(postId: number): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { title: 'Confirmer la suppression', message: 'Voulez-vous vraiment supprimer ce message ?' }
    });

    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.forumService.deletePost(postId).subscribe({
          next: () => {
            this.snackBar.open('Message supprimé.', 'OK');
            this.loadThread(); // Recharger
          },
          error: () => this.snackBar.open('Erreur lors de la suppression.', 'OK')
        });
      }
    });
  }

}
