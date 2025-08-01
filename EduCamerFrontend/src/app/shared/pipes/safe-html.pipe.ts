import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Pipe({
  name: 'safeHtml',
  standalone: true 
})
export class SafeHtmlPipe implements PipeTransform {

  constructor(private sanitizer: DomSanitizer) {}

  transform(value: string | null | undefined): SafeHtml {
    // Si la valeur est null ou undefined, retourner une chaîne vide sécurisée
    if (value == null) {
      return this.sanitizer.bypassSecurityTrustHtml('');
    }
    // Indiquer à Angular que ce HTML est sûr et peut être rendu tel quel
    return this.sanitizer.bypassSecurityTrustHtml(value);
  }
}