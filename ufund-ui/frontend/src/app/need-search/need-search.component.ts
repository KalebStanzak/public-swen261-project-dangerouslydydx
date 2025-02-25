import { Component, OnInit } from '@angular/core';

import { Observable, Subject } from 'rxjs';

import {
   debounceTime, distinctUntilChanged, switchMap
 } from 'rxjs/operators';

import { Need } from '../need';
import { NeedService } from '../need.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-need-search',
  templateUrl: './need-search.component.html',
  styleUrls: [ './need-search.component.css' ]
})
export class NeedSearchComponent implements OnInit {
  private searchTerms = new Subject<string>();

  constructor(
    private needService: NeedService, 
    private router: Router
  ) {}

  // Push a search term into the observable stream.
  search(term: string): void {
    this.searchTerms.next(term);
  }

  needs$!: Observable<Need[]>;

  ngOnInit(): void {
    this.needs$ = this.searchTerms.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap((term: string) => this.needService.searchNeeds(term)),
    );
  }
}