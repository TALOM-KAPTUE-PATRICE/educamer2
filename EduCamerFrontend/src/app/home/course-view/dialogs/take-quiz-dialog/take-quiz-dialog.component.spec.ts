import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TakeQuizDialogComponent } from './take-quiz-dialog.component';

describe('TakeQuizDialogComponent', () => {
  let component: TakeQuizDialogComponent;
  let fixture: ComponentFixture<TakeQuizDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TakeQuizDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TakeQuizDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
