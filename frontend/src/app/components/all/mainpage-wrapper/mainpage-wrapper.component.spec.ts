import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MainpageWrapperComponent } from './mainpage-wrapper.component';

describe('MainpageWrapperComponent', () => {
  let component: MainpageWrapperComponent;
  let fixture: ComponentFixture<MainpageWrapperComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MainpageWrapperComponent]
    });
    fixture = TestBed.createComponent(MainpageWrapperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
