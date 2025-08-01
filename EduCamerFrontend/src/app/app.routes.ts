import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WelcomeComponent } from './welcome/welcome.component';
import { authGuard } from './core/guards/auth.guard';
import { BecomeInstructorComponent } from './become-instructor/become-instructor.component';


export const routes: Routes = [

    { path: 'welcome' , component: WelcomeComponent},
    { path: '' , redirectTo: '/welcome' , pathMatch : 'full'},
    { path: 'auth' , loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule)},
    { path: 'home' , loadChildren: () => import('./home/home.module').then(m => m.HomeModule), canActivate: [authGuard]},
    { path: 'become-instructor', component: BecomeInstructorComponent },
    
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
  })
  
export class AppRoutingModule { }
  