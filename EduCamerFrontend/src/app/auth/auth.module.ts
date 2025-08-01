import { Component, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginParentComponent } from './login-parent/login-parent.component';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatDividerModule } from '@angular/material/divider';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatSidenavModule } from '@angular/material/sidenav';
import { CarouselModule } from 'ngx-owl-carousel-o';
import { MatRadioModule } from '@angular/material/radio';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule } from '@angular/material/snack-bar';



const AuthRoutes: Routes = [
  {
    path: '',
    component: LoginParentComponent,
    children: [
      { path: '', component: LoginComponent},
      { path: 'register', component: RegisterComponent },
      { path: 'forgot-password', component: ForgotPasswordComponent },
      { path: 'reset-password', component: ResetPasswordComponent },  
    ]
  },
]

@NgModule({
  declarations: [
    LoginComponent,
    LoginParentComponent,
    RegisterComponent,
    ForgotPasswordComponent,
    ResetPasswordComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(AuthRoutes),
    MatSlideToggleModule,
    MatButtonModule,
    MatCheckboxModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatIconModule,
    RouterModule,

    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatExpansionModule,
    MatGridListModule,
    CarouselModule,
    MatSidenavModule,
    MatListModule,  
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatDividerModule,
    MatRadioModule ,
  
    
   

  ]
})
export class AuthModule { }
