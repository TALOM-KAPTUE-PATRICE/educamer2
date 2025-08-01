import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChildProgressDialogComponent } from './child-progress-dialog.component';

describe('ChildProgressDialogComponent', () => {
  let component: ChildProgressDialogComponent;
  let fixture: ComponentFixture<ChildProgressDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChildProgressDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChildProgressDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
