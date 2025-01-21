import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserNeedDetailComponent } from './user-need-detail/user-need-detail.component';
import { AdminNeedDetailComponent } from './admin-need-detail/admin-need-detail.component';
import { FavoriteNeedsComponent } from './favorite-needs/favorite-needs.component';
import { LoginComponent } from './login/login.component';
import { SubscribeComponent } from './subscribe/subscribe.component';
import { BasketComponent } from './basket/basket.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { AdminNeedsComponent } from './admin-needs/admin-needs';
import { UserNeedsComponent } from './user-needs/user-needs.component';
import { SignupComponent } from './signup/signup.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent},
  { path: 'signup', component: SignupComponent}, 
  { path: 'favorites', component: FavoriteNeedsComponent},
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'basket', component: BasketComponent},
  { path: 'checkout', component: CheckoutComponent},
  { path: 'subscribe', component: SubscribeComponent },
    // Admin Routes
    {
      path: 'admin',
      children: [
        { path: 'needs', component: AdminNeedsComponent },
        { path: 'needs/:id', component: AdminNeedDetailComponent },  // View details for a specific need
      ]
    },
  
    // User Routes
    {
      path: 'user',
      children: [
        { path: 'needs', component: UserNeedsComponent },
        { path: 'needs/:id', component: UserNeedDetailComponent },  // View details for a specific need
      ]
    },
  
    { path: '**', redirectTo: 'login' }  // Redirect any unknown paths to login
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
