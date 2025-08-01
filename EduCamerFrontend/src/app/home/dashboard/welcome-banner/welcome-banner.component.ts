import { Component, Input } from '@angular/core';
import { CurrentUser } from '../../../auth/auth.service';


@Component({
  selector: 'app-welcome-banner',
  standalone: false,
  templateUrl: './welcome-banner.component.html',
  styleUrl: './welcome-banner.component.scss'
})
export class WelcomeBannerComponent {

    @Input() user: CurrentUser | null = null;

  getWelcomeMessage(role: string): string {
    const messages = {
      Eleve: "Prêt à apprendre quelque chose de nouveau aujourd'hui ?",
      Instructeur: "C'est un excellent jour pour inspirer vos élèves.",
      Parent: "Suivez la progression et soutenez le parcours de votre enfant.",
      default: "Ravi de vous revoir sur la plateforme."
    };
    return messages[role as keyof typeof messages] || messages.default;
  }

  getActionText(role: string): string {
    const actions = {
      Eleve: "Voir mon prochain cours",
      Instructeur: "Créer une nouvelle leçon",
      Parent: "Consulter le rapport",
      default: "Explorer"
    };
    return actions[role as keyof typeof actions] || actions.default;
  }

  getActionIcon(role: string): string {
    const icons = {
      Eleve: "play_circle_outline",
      Instructeur: "add_circle_outline",
      Parent: "bar_chart",
      default: "explore"
    };
    return icons[role as keyof typeof icons] || icons.default;
  }

}
