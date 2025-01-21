import { Component } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-admin-navbar',
  templateUrl: './admin-navbar.component.html',
  styleUrl: './admin-navbar.component.css'
})
export class AdminNavbarComponent {
  currentUser: User | null = null;

  constructor(private http: HttpClient, private userService: UserService) {}

  logout(): void{
    if (this.currentUser != null){
      this.userService.logout();
    }
  }
}
