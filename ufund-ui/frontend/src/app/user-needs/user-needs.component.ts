import { Component } from '@angular/core';
import { Need } from '../need';
import { NeedService } from '../need.service';
import { MessageService } from '../message.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-user-needs',
  templateUrl: './user-needs.component.html',
  styleUrl: './user-needs.component.css'
})
export class UserNeedsComponent {
  needs: Need[] = [];

  constructor( private needService: NeedService) {}

  getNeeds(): void {
    this.needService.getNeeds().subscribe(needs => this.needs = needs);
  }

  ngOnInit(): void {
    this.getNeeds();
  }

  add(name: string): void {
    name = name.trim();
    if (!name) { return; }
    this.needService.addNeed({ name } as Need)
      .subscribe(need => {
        this.needs.push(need);
      });
  }

}
