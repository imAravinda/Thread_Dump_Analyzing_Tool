import { TestBed } from '@angular/core/testing';

import { PageRefreshService } from './page-refresh.service';

describe('PageRefreshService', () => {
  let service: PageRefreshService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PageRefreshService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
