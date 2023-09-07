import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateReviewComponent } from './update-review.component';

describe('UpdateReviewComponent', () => {
  let component: UpdateReviewComponent;
  let fixture: ComponentFixture<UpdateReviewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UpdateReviewComponent]
    });
    fixture = TestBed.createComponent(UpdateReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
