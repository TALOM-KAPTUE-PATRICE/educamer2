import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-stats-card',
  standalone: false,
  templateUrl: './stats-card.component.html',
  styleUrl: './stats-card.component.scss'
})
export class StatsCardComponent {

  @Input() icon: string = 'help_outline';
  @Input() value: string = '0';
  @Input() label: string = 'Label';
   // Syntaxe correcte pour un input requis
  @Input({ required: true }) color: 'blue' | 'green' | 'orange' | 'purple' = 'blue'; // <<< CORRIGÉ
  @Input() progressValue?: number;

}
