import { Component, Input } from '@angular/core';
import { Need } from '../need';
import {NgIf, UpperCasePipe} from '@angular/common';
import {FormsModule} from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { AdminNavbarComponent } from '../admin-navbar/admin-navbar.component';

import { NeedService } from '../need.service';

@Component({
  selector: 'app-admin-need-detail',
  templateUrl: './admin-need-detail.component.html',
  styleUrl: './admin-need-detail.component.css'
})
export class AdminNeedDetailComponent {
  @Input() need?: Need;

  constructor(
    private route: ActivatedRoute,
    private needService: NeedService,
    private location: Location
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

}
