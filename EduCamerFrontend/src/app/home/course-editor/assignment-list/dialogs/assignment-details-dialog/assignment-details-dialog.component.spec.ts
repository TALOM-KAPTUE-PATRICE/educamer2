import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignmentDetailsDialogComponent } from './assignment-details-dialog.component';

describe('AssignmentDetailsDialogComponent', () => {
  let component: AssignmentDetailsDialogComponent;
  let fixture: ComponentFixture<AssignmentDetailsDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AssignmentDetailsDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AssignmentDetailsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
