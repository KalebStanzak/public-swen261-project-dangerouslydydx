import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MessagesComponent } from './messages/messages.component';
import { FavoriteNeedsComponent } from './favorite-needs/favorite-needs.component';
import { NeedSearchComponent } from './need-search/need-search.component';
import { LoginComponent } from './login/login.component';
import { AdminNavbarComponent } from './admin-navbar/admin-navbar.component';
import { UserNavbarComponent } from './user-navbar/user-navbar.component'
import { BasketComponent } from './basket/basket.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { AdminNeedsComponent } from './admin-needs/admin-needs';
import { UserNeedsComponent } from './user-needs/user-needs.component';
import { AdminNeedDetailComponent } from './admin-need-detail/admin-need-detail.component';
import { UserNeedDetailComponent } from './user-need-detail/user-need-detail.component';
import { SignupComponent } from './signup/signup.component';
import { SubscribeComponent } from './subscribe/subscribe.component';

@NgModule({
  declarations: [
    AppComponent,
    AdminNeedDetailComponent,
    UserNeedDetailComponent,
    MessagesComponent,
    FavoriteNeedsComponent,
    NeedSearchComponent,
    LoginComponent,
    BasketComponent,
    CheckoutComponent,
    AdminNavbarComponent,
    AdminNeedsComponent,
    UserNavbarComponent,
    UserNeedsComponent,
    SignupComponent,
    SubscribeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
