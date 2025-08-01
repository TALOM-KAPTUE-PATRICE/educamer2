import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GradebookTabComponent } from './gradebook-tab.component';

describe('GradebookTabComponent', () => {
  let component: GradebookTabComponent;
  let fixture: ComponentFixture<GradebookTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GradebookTabComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GradebookTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
