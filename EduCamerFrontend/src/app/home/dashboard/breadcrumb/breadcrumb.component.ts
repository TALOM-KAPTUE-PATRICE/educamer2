import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Breadcrumb, BreadcrumbService } from '../../../core/services/breadcrumb.service';


@Component({
  selector: 'app-breadcrumb',
  standalone: false,
  templateUrl: './breadcrumb.component.html',
  styleUrl: './breadcrumb.component.scss'
})
export class BreadcrumbComponent {
  

  breadcrumbs$: Observable<Breadcrumb[]>;
  constructor(private breadcrumbService: BreadcrumbService) {
    this.breadcrumbs$ = this.breadcrumbService.breadcrumbs$;
  }

}
