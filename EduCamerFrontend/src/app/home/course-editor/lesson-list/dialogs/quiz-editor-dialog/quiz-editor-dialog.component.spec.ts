import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuizEditorDialogComponent } from './quiz-editor-dialog.component';

describe('QuizEditorDialogComponent', () => {
  let component: QuizEditorDialogComponent;
  let fixture: ComponentFixture<QuizEditorDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuizEditorDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QuizEditorDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
