import { Component, OnInit } from '@angular/core';
import { Basket } from '../basket';
import { BasketService } from '../basket.service';
import { Need } from '../need';
import { MessageService } from '../message.service';

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrl: './basket.component.css'
})
export class BasketComponent implements OnInit {

  basket: Basket | undefined;

  ngOnInit(): void {
    this.getBasket();
  }

  constructor(
    private basketService: BasketService, 
    private messageService: MessageService
  ) {}

  getBasket(): void {
    console.log("get basket called in basket component")
    this.basketService.getBasket().subscribe({
      next: (basket) => {console.log('Fetched needs:', basket); // Check what is fetched
      this.basket = basket;
    },
      error: (err) => console.error('Error fetching basket:', err)
    });
  }

  remove(need: Need): void {
    if (this.basket) {
    this.basket.needs = this.basket?.needs.filter(n => n !== need);
    this.basketService.removeNeedFromBasket(need.id).subscribe();
    console.log("need with id " + need.id + " removed from basket");
    }
  }
}
