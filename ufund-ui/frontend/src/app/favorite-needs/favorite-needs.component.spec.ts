import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavoriteNeedsComponent } from './favorite-needs.component';

describe('FavoriteNeedsComponent', () => {
  let component: FavoriteNeedsComponent;
  let fixture: ComponentFixture<FavoriteNeedsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FavoriteNeedsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FavoriteNeedsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
