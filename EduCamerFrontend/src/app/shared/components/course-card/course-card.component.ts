import { Component, Input } from '@angular/core';
import { CatalogCourse, EnrolledCourseSummary } from '../../../core/models/student.model';

@Component({
  selector: 'app-course-card',
  standalone: false,
  templateUrl: './course-card.component.html',
  styleUrl: './course-card.component.scss'
})
export class CourseCardComponent {

    // Déclare que ce composant peut recevoir une propriété 'course' de son parent.
  // L'opérateur '!' indique à TypeScript que cette propriété sera toujours fournie.
   @Input() course!: CatalogCourse | EnrolledCourseSummary;
   @Input() isEnrolled: boolean = false;


  constructor() { }

    /**
   * Type Guard: Vérifie si le cours est un EnrolledCourseSummary.
   * Le 'course is EnrolledCourseSummary' est la syntaxe spéciale qui informe TypeScript.
   */
  isEnrolledCourse(course: CatalogCourse | EnrolledCourseSummary): course is EnrolledCourseSummary {
    return (course as EnrolledCourseSummary).progress !== undefined;
  }

  /**
   * Type Guard: Vérifie si le cours est un CatalogCourse.
   */
  isCatalogCourse(course: CatalogCourse | EnrolledCourseSummary): course is CatalogCourse {
    return (course as CatalogCourse).averageRating !== undefined;
  }

}
