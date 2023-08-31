import { TestBed } from '@angular/core/testing';

import { EntertainmentService } from './entertainment.service';

describe('EntertainmentService', () => {
  let service: EntertainmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EntertainmentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
