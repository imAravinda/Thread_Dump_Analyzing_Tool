import { TestBed } from '@angular/core/testing';

import { AnayzeResultCacheService } from './anayze-result-cache.service';

describe('AnayzeResultCacheService', () => {
  let service: AnayzeResultCacheService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AnayzeResultCacheService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
