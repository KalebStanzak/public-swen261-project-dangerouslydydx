import { Component } from '@angular/core';
import { Need } from '../need'
import { NeedService } from '../need.service';
import { NgFor } from '@angular/common'
import { MessageService } from '../message.service';

@Component({
  selector: 'app-admin-needs',
  templateUrl: './admin-needs.component.html',
  styleUrl: './admin-needs.component.css'
})
export class AdminNeedsComponent {
  needs: Need[] = [];

  constructor(private needService: NeedService, private messageService: MessageService) {}

  getNeeds(): void {
    this.needService.getNeeds().subscribe(needs => this.needs = needs);
  }

  ngOnInit(): void {
    this.getNeeds();
  }

  // add(name: string): void {
  //   name = name.trim();
  //   if (!name) { return; }
  //   this.needService.addNeed({ name } as Need)
  //     .subscribe(need => {
  //       this.needs.push(need);
  //     });
  // }

  add(name: string, cost: number, quantity: number, type: string): void {
    name = name.trim();
    if (!name) { return; }
    console.log('Adding need to cupboard:', name, cost, quantity, type);
    this.needService.addNeed({ name, cost, quantity, type } as Need)
      .subscribe(need => {
        this.needs.push(need);
      });
  }

  delete(need: Need): void {
    this.needs = this.needs.filter(n => n !== need);
    this.needService.deleteNeed(need.id).subscribe();
  }
}