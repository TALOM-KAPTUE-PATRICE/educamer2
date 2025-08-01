import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { NgxExtendedPdfViewerModule } from 'ngx-extended-pdf-viewer';

@Component({
  selector: 'app-pdf-viewer-dialog',
  standalone: true,
  imports: [
    MatDialogModule,             
    NgxExtendedPdfViewerModule  
  ],
  templateUrl: './pdf-viewer-dialog.component.html',
  styleUrl: './pdf-viewer-dialog.component.scss'
})
export class PdfViewerDialogComponent {

  constructor(@Inject(MAT_DIALOG_DATA) public data: { url: string, name: string }) {}


}
