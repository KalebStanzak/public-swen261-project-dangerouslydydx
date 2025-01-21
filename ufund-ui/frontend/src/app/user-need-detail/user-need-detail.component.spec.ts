import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserNeedDetailComponent } from './user-need-detail.component';

describe('NeedDetailComponent', () => {
  let component: UserNeedDetailComponent;
  let fixture: ComponentFixture<UserNeedDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserNeedDetailComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserNeedDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
