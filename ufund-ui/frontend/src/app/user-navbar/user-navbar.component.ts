import { Component } from '@angular/core';
import { User } from '../user';
import { HttpClient } from '@angular/common/http';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { BasketService } from '../basket.service';
import { NeedService } from '../need.service';

@Component({
  selector: 'app-user-navbar',
  templateUrl: './user-navbar.component.html',
  styleUrl: './user-navbar.component.css'
})
export class UserNavbarComponent {
  currentUser: User | null = null;

  constructor(
    private http: HttpClient, 
    private userService: UserService, 
    private router: Router,
    private basketService: BasketService,
    private needService: NeedService
  ) {}

  logout(): void{
    if (this.currentUser != null){
      this.router.navigate(['login']);
      this.userService.logout();
    }
  }

  // checkout(): void {
  //   if (this.currentUser != null){
  //     this.needService.checkout();
  //     this.userService.checkout();
  //     this.basketService.checkout();
  //   }
  // }
}
