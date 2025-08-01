import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-image-viewer-dialog',
  standalone: true,
  templateUrl: './image-viewer-dialog.component.html',
  styleUrl: './image-viewer-dialog.component.scss'
})
export class ImageViewerDialogComponent {

  constructor(@Inject(MAT_DIALOG_DATA) public data: { url: string, name: string }) {}

}
