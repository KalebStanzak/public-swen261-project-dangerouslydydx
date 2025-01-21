import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-subscribe',
  templateUrl: './subscribe.component.html',
  styleUrls: ['./subscribe.component.css']
})
export class SubscribeComponent {
  successMessage: string | null = null;

  constructor(private http: HttpClient) {}

  onSubmit(form: any) {
    const email = form.value.email;
    if (email) {
      this.http.post('http://localhost:2006/subscribe', { email }).subscribe(
        response => {
          this.successMessage = 'Thank you for subscribing!';
        },
        error => {
          console.error('Error subscribing', error);
          this.successMessage = 'There was an error. Please try again later.';
        }
      );
    }
  }
}
