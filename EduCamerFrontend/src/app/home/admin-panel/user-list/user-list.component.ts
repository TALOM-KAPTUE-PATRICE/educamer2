import { Component, OnInit } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { AdminService, ManagedUser } from '../../../core/services/admin.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../../dialogs/confirm-dialog/confirm-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';



@Component({
  selector: 'app-user-list',
  standalone: false,
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {
  
  users$!: Observable<ManagedUser[]>;
  displayedColumns: string[] = ['avatar', 'name', 'email', 'role','status', 'actions'];
  
  // Utiliser un BehaviorSubject pour pouvoir rafraîchir la liste après une suppression
  private refresh$ = new BehaviorSubject<void>(undefined);

  constructor(
    private adminService: AdminService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.users$ = this.refresh$.pipe(
      switchMap(() => this.adminService.getAllUsers())
    );
  }

    // --- NOUVELLES MÉTHODES ---
  toggleUserStatus(user: ManagedUser): void {
    const action = user.enabled ? this.adminService.disableUser(user.id) : this.adminService.enableUser(user.id);
    const actionText = user.enabled ? 'désactivé' : 'activé';

    action.subscribe({
      next: () => {
        this.snackBar.open(`L'utilisateur "${user.name}" a été ${actionText}.`, 'OK', { duration: 3000 });
        this.refresh$.next(); // Rafraîchir la liste pour voir le nouveau statut
      },
      error: (err) => {
        console.error(`Erreur lors de la modification du statut de l'utilisateur:`, err);
      }
    });
  }

  deleteUser(userId: number, userName: string): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Confirmer la Suppression',
        message: `Êtes-vous sûr de vouloir supprimer définitivement l'utilisateur "${userName}" ? Cette action est irréversible.`
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === true) { // Si l'admin a cliqué sur "Confirmer"
        this.adminService.deleteUser(userId).subscribe({
          next: () => {
            this.snackBar.open(`L'utilisateur "${userName}" a été supprimé.`, 'OK', { duration: 3000 });
            this.refresh$.next(); // Rafraîchir la liste
          },
          error: (err) => {
            console.error('Erreur lors de la suppression de l\'utilisateur:', err);
          }
        });
      }
    });
  }
}