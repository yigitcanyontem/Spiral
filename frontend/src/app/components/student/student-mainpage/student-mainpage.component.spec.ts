import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentMainpageComponent } from './student-mainpage.component';

describe('StudentMainpageComponent', () => {
  let component: StudentMainpageComponent;
  let fixture: ComponentFixture<StudentMainpageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StudentMainpageComponent]
    });
    fixture = TestBed.createComponent(StudentMainpageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
