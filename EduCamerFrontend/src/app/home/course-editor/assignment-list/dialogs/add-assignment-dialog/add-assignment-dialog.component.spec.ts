import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddAssignmentDialogComponent } from './add-assignment-dialog.component';

describe('AddAssignmentDialogComponent', () => {
  let component: AddAssignmentDialogComponent;
  let fixture: ComponentFixture<AddAssignmentDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddAssignmentDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddAssignmentDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
