import { Directive, ElementRef, Input, OnInit, OnDestroy, NgZone } from '@angular/core';

@Directive({
  selector: '[appAnimateOnScroll]',
  standalone: true
})
export class AnimateOnScrollDirective implements OnInit, OnDestroy {
  private observer!: IntersectionObserver;

  @Input('data-animation-delay') delay: string = '0s';

  constructor(private el: ElementRef, private zone: NgZone) {}

  ngOnInit(): void {
    this.el.nativeElement.style.transitionDelay = this.delay;
    this.initObserver();
  }

  ngOnDestroy(): void {
    if (this.observer) {
      this.observer.disconnect();
    }
  }

  private initObserver(): void {
    const options = {
      root: null,
      rootMargin: '0px',
      threshold: 0.2 // Se déclenche quand 20% de l'élément est visible
    };

    this.zone.runOutsideAngular(() => {
      this.observer = new IntersectionObserver(entries => {
        entries.forEach(entry => {
          // ▼▼▼ LA LOGIQUE EST MODIFIÉE ICI ▼▼▼
          if (entry.isIntersecting) {
            // Si l'élément entre dans la vue, on ajoute la classe pour l'animer.
            this.zone.run(() => {
              entry.target.classList.add('is-visible');
            });
            // On ne fait PAS 'unobserve' pour continuer à le surveiller.
          } else {
            // Si l'élément SORT de la vue, on retire la classe.
            // L'élément est maintenant prêt à être ré-animé la prochaine fois qu'il entrera.
            this.zone.run(() => {
              entry.target.classList.remove('is-visible');
            });
          }
          // ▲▲▲ FIN DE LA MODIFICATION ▲▲▲
        });
      }, options);

      this.observer.observe(this.el.nativeElement);
    });
  }
}