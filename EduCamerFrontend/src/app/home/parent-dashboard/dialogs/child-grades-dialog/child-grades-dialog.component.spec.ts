import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChildGradesDialogComponent } from './child-grades-dialog.component';

describe('ChildGradesDialogComponent', () => {
  let component: ChildGradesDialogComponent;
  let fixture: ComponentFixture<ChildGradesDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChildGradesDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChildGradesDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
