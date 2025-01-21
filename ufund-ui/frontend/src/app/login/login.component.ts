// src/app/login/login.component.ts
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../user.service';
// import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  message: string = '';

  constructor(
    private http: HttpClient, 
    private router: Router, 
    private userService: UserService) {}

  login() {
    if (!(this.username == "admin")) {
      console.log(`Logging in with username: ${this.username}`);
      this.userService.login(this.username).subscribe(
        (response) => {
          console.log('Login successful:', response);
          this.router.navigate(["/user/needs"]); 
        },
        (error) => {
          console.error('An error occurred during login:', error);
          alert("username does not exist!");
        }
      );
    } else if (this.username == "admin") {
      console.log('Logging in as admin');
      this.router.navigate(["/admin/needs"])
    } else {
      console.log('Username is required.');
    }
  }

}
