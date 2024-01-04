import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DumpResultComponent } from './dump-result.component';

describe('DumpResultComponent', () => {
  let component: DumpResultComponent;
  let fixture: ComponentFixture<DumpResultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DumpResultComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DumpResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
