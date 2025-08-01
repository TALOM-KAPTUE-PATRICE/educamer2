import { Component } from '@angular/core';
import { OwlOptions } from 'ngx-owl-carousel-o';

@Component({
  selector: 'app-welcome',
  standalone: false,
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})
export class WelcomeComponent {

  // Configuration optimisée pour le carrousel de témoignages
  testimonialOptions: OwlOptions = {
    loop: true,
    mouseDrag: true,
    touchDrag: true,
    pullDrag: false,
    dots: true,
    navSpeed: 700,
    navText: [
      '<i class="mat-icon material-icons">keyboard_arrow_left</i>', 
      '<i class="mat-icon material-icons">keyboard_arrow_right</i>'
    ],
    responsive: {
      0: {
        items: 1
      },
      768: {
        items: 2
      },
      1200: {
        items: 3
      }
    },
    nav: true,
    autoplay: true,
    autoplayTimeout: 5000,
    autoplayHoverPause: true
  };

  // Données des témoignages enrichies avec des rôles et des notations
  testimonials = [
    {
      image: 'assets/welcome/student1.jpg',
      quote: "La progression séquentielle des leçons m'a vraiment aidé à ne pas me sentir perdu. J'ai validé mon quiz de maths et j'ai enfin compris le chapitre !",
      author: "Fanta B.",
      role: "Élève en 3ème",
      rating: 5
    },
    {
      image: 'assets/welcome/parent1.jpg',
      quote: "Je suis impressionné par la qualité du suivi. Voir les notes et la progression de ma fille en temps réel est un vrai plus. C'est rassurant.",
      author: "M. Ndiaye",
      role: "Parent d'élève",
      rating: 5
    },
    {
      image: 'assets/welcome/instructor1.jpg',
      quote: "En tant qu'enseignant, la plateforme me donne des outils incroyables pour créer du contenu interactif. L'éditeur de cours est simple et puissant.",
      author: "Mme. Kamga",
      role: "Instructrice de Physique",
      rating: 4
    },
    {
      image: 'assets/welcome/student2.jpg',
      quote: "Les ressources PDF et les vidéos sont toujours disponibles. Je peux réviser n'importe quand, même depuis mon téléphone. Ça a changé ma façon d'étudier.",
      author: "Ahmed K.",
      role: "Élève en Terminale",
      rating: 5
    }
  ];

  // Fonction utilitaire pour générer un tableau d'étoiles pour l'affichage
  getStars(rating: number): number[] {
    return Array(rating).fill(0);
  }
}