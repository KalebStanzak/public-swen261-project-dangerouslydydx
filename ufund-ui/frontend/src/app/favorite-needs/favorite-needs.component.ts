import { Component, OnInit } from '@angular/core';
import { Need } from '../need';
import { NeedService } from '../need.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-favorite-needs',
  templateUrl: './favorite-needs.component.html',
  styleUrls: [ './favorite-needs.component.css' ]
})
export class FavoriteNeedsComponent implements OnInit {
  favorites: Need[] = [];

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.getFavorites();
  }

  getFavorites(): void {
    this.userService.getFavorites().subscribe({
      next: (favorites) => {
        this.favorites = favorites;
        console.log('Fetched favorites:', favorites);
      },
      error: (error) => {
        console.error('Error fetching favorites:', error);
      },
    });
  }
}