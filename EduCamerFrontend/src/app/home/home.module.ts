import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms'; // Utile pour le profil

// Import du routage
import { HOME_ROUTES } from './home.routes';

// Import des modules Angular Material utilisés dans ce module
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatSelectModule } from '@angular/material/select';
import { MatBadgeModule } from '@angular/material/badge';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatTabsModule } from '@angular/material/tabs';
import { MatInputModule } from '@angular/material/input';
import { MatDividerModule } from '@angular/material/divider';

import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
// Import des composants du Layout
import { DashboardLayoutComponent } from '../layouts/dashboard-layout/dashboard-layout.component';
import { FooterComponent } from '../layouts/components/footer/footer.component';
import { SidebarComponent } from '../layouts/components/sidebar/sidebar.component';
import { HeaderComponent } from '../layouts/components/header/header.component';

// Import des composants de Page
import { DashboardComponent } from './dashboard/dashboard.component';
import { StatsCardComponent } from './dashboard/stats-card/stats-card.component';
import { WelcomeBannerComponent } from './dashboard/welcome-banner/welcome-banner.component';
import { AdminPanelComponent } from './admin-panel/admin-panel.component';
import { MyCoursesComponent } from './my-courses/my-courses.component';
import { CatalogComponent } from './catalog/catalog.component';
import { MyGradesComponent } from './my-grades/my-grades.component';
import { CourseManagementComponent } from './course-management/course-management.component';
import { ProfileComponent } from './profile/profile.component';
import { MatTooltipModule } from '@angular/material/tooltip'; // <-- AJOUTEZ}
import { MatMenuModule } from '@angular/material/menu'; // <-- AJOUTEZ
import { ApplicationDetailComponent } from './admin-panel/application-detail/application-detail.component';
import { ApplicationListComponent } from './admin-panel/application-list/application-list.component';
import { MatTableModule } from '@angular/material/table';
import { RejectionDialogComponent } from '../dialog/rejection-dialog/rejection-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { UserListComponent } from './admin-panel/user-list/user-list.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CreateCourseDialogComponent } from './course-management/dialogs/create-course-dialog/create-course-dialog.component';
import { CourseEditorComponent } from './course-editor/course-editor.component';
import { LessonListComponent } from './course-editor/lesson-list/lesson-list.component';
import { AssignmentListComponent } from './course-editor/assignment-list/assignment-list.component';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatChipsModule } from '@angular/material/chips'; // <-- AJOUTEZ CET IMPORT
import { SettingsTabComponent } from './course-editor/settings-tab/settings-tab.component';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { ResourceListComponent } from './course-editor/lesson-list/resource-list/resource-list.component';
import { AddLessonDialogComponent } from './course-editor/lesson-list/dialogs/add-lesson-dialog/add-lesson-dialog.component';
import { AddResourceDialogComponent } from './course-editor/lesson-list/dialogs/add-resource-dialog/add-resource-dialog.component';
import { QuizEditorDialogComponent } from './course-editor/lesson-list/dialogs/quiz-editor-dialog/quiz-editor-dialog.component';
import { AddAssignmentDialogComponent } from './course-editor/assignment-list/dialogs/add-assignment-dialog/add-assignment-dialog.component';
import { CourseCardComponent } from '../shared/components/course-card/course-card.component';
import { CourseViewComponent } from './course-view/course-view.component';
import { SafeHtmlPipe } from '../shared/pipes/safe-html.pipe';
import { CourseTrackingComponent } from './course-tracking/course-tracking.component';
import { ProgressTabComponent } from './course-tracking/progress-tab/progress-tab.component';
import { GradebookTabComponent } from './course-tracking/gradebook-tab/gradebook-tab.component';
import { ForumComponent } from './forum/forum.component';
import { ForumThreadComponent } from './forum/forum-thread/forum-thread.component';
import { EditPostDialogComponent } from './forum/dialogs/edit-post-dialog/edit-post-dialog.component';
import { HelpComponent } from './help/help.component';
import { CreateThreadDialogComponent } from './forum/dialogs/create-thread-dialog/create-thread-dialog.component';
import { TakeQuizDialogComponent } from './course-view/dialogs/take-quiz-dialog/take-quiz-dialog.component';
import { MatRadioButton, MatRadioModule } from '@angular/material/radio';
import { ParentDashboardComponent } from './parent-dashboard/parent-dashboard.component';
import { ChildCardComponent } from './parent-dashboard/child-card/child-card.component';
import { ChildProgressDialogComponent } from './parent-dashboard/dialogs/child-progress-dialog/child-progress-dialog.component';
import { ChildGradesDialogComponent } from './parent-dashboard/dialogs/child-grades-dialog/child-grades-dialog.component';
import { BreadcrumbComponent } from './dashboard/breadcrumb/breadcrumb.component';
import { NgxExtendedPdfViewerModule } from 'ngx-extended-pdf-viewer';
import { PdfViewerDialogComponent } from './course-editor/lesson-list/resource-list/dialogs/pdf-viewer-dialog/pdf-viewer-dialog.component';
import { ImageViewerDialogComponent } from './course-editor/lesson-list/resource-list/dialogs/image-viewer-dialog/image-viewer-dialog.component';
import { AssignmentDetailsDialogComponent } from './course-editor/assignment-list/dialogs/assignment-details-dialog/assignment-details-dialog.component';
import { ChatbotComponent } from '../shared/components/chatbot/chatbot.component';
import { MatButtonToggleModule } from '@angular/material/button-toggle'; // <-- AJOUTEZ CET IMPORT
import { TextFieldModule } from '@angular/cdk/text-field'; // <-- AJOUTEZ CET 
import { ConfirmationDialogComponent } from '../shared/components/confirmation-dialog/confirmation-dialog.component';

@NgModule({
  declarations: [
    // Composants de Layout
    DashboardLayoutComponent,
    FooterComponent,
    SidebarComponent,
    HeaderComponent,
    UserListComponent,
    // Composants de Page et Enfants
    DashboardComponent,
    StatsCardComponent,
    WelcomeBannerComponent,
    AdminPanelComponent,
    MyCoursesComponent,
    CatalogComponent,
    MyGradesComponent,
    CourseManagementComponent,
    ProfileComponent,
    ApplicationDetailComponent,
    ApplicationListComponent,
    RejectionDialogComponent,
    CourseEditorComponent,
    CreateCourseDialogComponent,
    LessonListComponent,
    AssignmentListComponent,
    SettingsTabComponent,
    ResourceListComponent,
    AddLessonDialogComponent,
    AddResourceDialogComponent,
    QuizEditorDialogComponent,
    AddAssignmentDialogComponent,
    CourseCardComponent,
    CourseViewComponent,
    CourseTrackingComponent,
    ProgressTabComponent,
    GradebookTabComponent,
    ForumComponent,
    ForumThreadComponent,
    EditPostDialogComponent,
    HelpComponent,
    CreateThreadDialogComponent,
    TakeQuizDialogComponent,
    ParentDashboardComponent,
    ChildCardComponent,
    ChildProgressDialogComponent,
    ChildGradesDialogComponent,
    BreadcrumbComponent,
    AssignmentDetailsDialogComponent,
    ChatbotComponent,
    ConfirmationDialogComponent

  ],
  imports: [
    CommonModule,
    RouterModule.forChild(HOME_ROUTES), // Utilise le fichier de routes dédié
    ReactiveFormsModule,
    MatDialogModule,
    DragDropModule,
    TextFieldModule,
    MatToolbarModule, MatIconModule, MatFormFieldModule, MatCardModule,
    MatButtonModule, MatSidenavModule, MatListModule, MatSelectModule,
    MatBadgeModule, MatGridListModule, MatProgressBarModule, MatTabsModule,
    MatInputModule, MatDividerModule, MatMenuModule, MatTooltipModule, MatTableModule,
    MatProgressSpinnerModule, MatDialogModule, MatExpansionModule,
    MatChipsModule,
    SafeHtmlPipe,
    MatDatepickerModule,
    MatNativeDateModule,
    MatLabel,
    MatDialogModule,
    MatRadioModule,
     MatButtonToggleModule,
    MatNativeDateModule,
  




    







  ]
})
export class HomeModule { }