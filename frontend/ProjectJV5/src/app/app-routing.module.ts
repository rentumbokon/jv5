import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';


const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent
  },
  { path: '',
    redirectTo: '/login',
    pathMatch: 'full',
    
  },
  {
    path: 'login',
    component: LoginComponent,
  }
];

@NgModule({
  imports: [RouterModule.forRoot(
    routes,
    { enableTracing: false } // <-- debugging purposes only
  )],
  exports: [RouterModule]
})
export class AppRoutingModule { }