import { Component } from '@angular/core';
import { UserService } from '../user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {
  username: string = '';

  constructor(private userService: UserService, private router: Router) {}

  signup() {
    if (this.username) {
      this.userService.signup(this.username).subscribe(
        (response) => {
          console.log('Sign-up successful:', response);
          this.router.navigate(["/user/needs"]);
        },
        (error) => {
          console.error('Sign-up error:', error);
          // Handle error (e.g., show error message to the user)
        }
      );
    } else {
      console.log('Username is required');
    }
  }
}
