import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { OwlOptions } from 'ngx-owl-carousel-o';

@Component({
  selector: 'app-welcome',
  standalone: false,
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})
export class WelcomeComponent  {

    // --- 1. GESTION DU MENU MOBILE ---
  isMenuOpen = false;
  isToolbarScrolled = false;

    // --- 2. GESTION DES ANIMATIONS AU SCROLL ---
  // ▼▼▼ LA CORRECTION EST ICI ▼▼▼
  // On déclare la propriété 'observer' et on lui donne son type.
  private observer!: IntersectionObserver;
  // ▲▲▲ FIN DE LA CORRECTION ▲▲▲

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
      image: 'assets/welcome/profile.svg',
      quote: "La progression séquentielle des leçons m'a vraiment aidé à ne pas me sentir perdu. J'ai validé mon quiz de maths et j'ai enfin compris le chapitre !",
      author: "Fanta B.",
      role: "Élève en 3ème",
      rating: 5
    },
    {
      image: 'assets/welcome/profile.svg',
      quote: "Je suis impressionné par la qualité du suivi. Voir les notes et la progression de ma fille en temps réel est un vrai plus. C'est rassurant.",
      author: "M. Ndiaye",
      role: "Parent d'élève",
      rating: 5
    },
    {
      image: 'assets/welcome/profile.svg',
      quote: "En tant qu'enseignant, la plateforme me donne des outils incroyables pour créer du contenu interactif. L'éditeur de cours est simple et puissant.",
      author: "Mme. Kamga",
      role: "Instructrice de Physique",
      rating: 4
    },
    {
      image: 'assets/welcome/profile.svg',
      quote: "Les ressources PDF et les vidéos sont toujours disponibles. Je peux réviser n'importe quand, même depuis mon téléphone. Ça a changé ma façon d'étudier.",
      author: "Ahmed K.",
      role: "Élève en Terminale",
      rating: 5
    }
  ];

    // FAQ enrichie avec plus de questions pertinentes
  faqs = [
    {
      question: "La plateforme est-elle gratuite ?",
      answer: "L'inscription et l'accès à une sélection de cours de découverte sont entièrement gratuits. Des abonnements premium sont disponibles pour un accès illimité à tout le catalogue et au tutorat personnalisé."
    },
    {
      question: "Les cours sont-ils adaptés au programme camerounais ?",
      answer: "Oui, absolument. Tous nos contenus sont conçus par des enseignants expérimentés et sont rigoureusement alignés sur le programme scolaire officiel du Cameroun, de la 6ème à la Terminale."
    },
    {
      question: "Comment fonctionne le tutorat personnalisé ?",
      answer: "Lorsqu'un élève rencontre une difficulté, il peut soumettre une demande d'aide. Un tuteur qualifié dans la matière concernée prendra en charge la demande pour une session d'explication individuelle via notre plateforme."
    },
    {
      question: "En tant que parent, que puis-je voir exactement ?",
      answer: "Après avoir lié votre compte à celui de votre enfant, vous aurez accès à un tableau de bord complet indiquant sa progression dans chaque cours, ses notes aux devoirs et quiz, et le temps passé. C'est un outil puissant pour un accompagnement positif."
    },
    {
      question: "Puis-je utiliser EduCamer sur mon téléphone ou ma tablette ?",
      answer: "Oui, notre plateforme est entièrement responsive. Vous pouvez suivre vos cours, soumettre vos devoirs et interagir sur n'importe quel appareil : ordinateur, tablette ou smartphone."
    }
  ];

  constructor(private el: ElementRef) {}


  // --- MÉTHODES POUR LE MENU MOBILE ---
  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  closeMenu(): void {
    this.isMenuOpen = false;
  }

  // --- MÉTHODE POUR LE SCROLL FLUIDE ---
  scrollTo(sectionId: string, event: Event): void {
    event.preventDefault();
    document.getElementById(sectionId)?.scrollIntoView({ behavior: 'smooth' });
    this.closeMenu(); // Ferme le menu après avoir cliqué sur un lien
  }

  // --- MÉTHODE POUR LE STYLE DU HEADER AU SCROLL ---
  @HostListener('window:scroll', [])
  onWindowScroll(): void {
    this.isToolbarScrolled = window.scrollY > 10;
  }

  // Fonction utilitaire pour les étoiles (inchangée)
  getStars(rating: number): number[] {
    return Array(rating).fill(0);
  }
}