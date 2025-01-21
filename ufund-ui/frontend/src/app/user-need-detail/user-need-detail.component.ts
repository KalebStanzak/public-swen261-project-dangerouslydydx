import { Component, Input } from '@angular/core';
import { Need } from '../need';
import {NgIf, UpperCasePipe} from '@angular/common';
import {FormsModule} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';

import { NeedService } from '../need.service';
//import { UserService } from '../user.service';
import { BasketService } from '../basket.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-user-need-detail',
  templateUrl: './user-need-detail.component.html',
  styleUrl: './user-need-detail.component.css'
})
export class UserNeedDetailComponent {
  @Input() need?: Need;
  favorites: Need[] = [];

  constructor(
    private route: ActivatedRoute,
    private needService: NeedService,
    private location: Location,
    private basketService: BasketService,
    private router: Router,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.getNeed();
  }
  
  getNeed(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.needService.getNeed(id)
      .subscribe(need => this.need = need);
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    if (this.need) {
      this.needService.updateNeed(this.need)
        .subscribe(() => this.goBack());
    }
  }

  addNeedToBasket(): void {
    console.log("add need called");
    if (this.need) {
      console.log("need exists.");
      this.basketService.addNeedToBasket(this.need.id, this.need).subscribe({
        next: (response) => { 
          console.log('Basket updated:', response);
          alert(`${this.need?.name} added to basket!`);
        },
        error: (err) => console.error('Error adding need to basket:', err)
      });
      this.router.navigate(['/basket']);
    }
  }

  addToFavorites(): void {
    if(this.need){
      console.log("need exists (addToFavorites)");
      this.userService.addFavorite(this.need).subscribe({
        next: (favorites) => {
          console.log('Favorites updated:', favorites);
          alert(`${this.need?.name} added to your favorites!`);
        },
        error: (error) => {
          console.error('Error adding favorite:', error);
          alert('Failed to add favorite. Please try again.');
        }
      });
    }
  }

  removeFromFavorites(need: Need): void {
    this.userService.removeFromFavorites(need).subscribe({
      next: (favorites) => {
        console.log('Updated favorites:', favorites);
        alert(`${need.name} has been removed from favorites.`);
      },
      error: (error) => {
        console.error('Error removing favorite:', error);
      },
    });
  }

}
