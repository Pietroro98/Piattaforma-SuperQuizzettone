import { Component, inject } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { QuestionService } from '../../core/service/question.service';

@Component({
  selector: 'app-question-creator',
  imports: [ReactiveFormsModule],
  templateUrl: './question-creator.component.html',
  styleUrl: './question-creator.component.scss'
})
export class QuestionCreatorComponent {

  form: FormGroup;
  private readonly questionService = inject(QuestionService);

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      description: ['', Validators.required],
      type: ['SINGOLA'],
      categoryMode: ['select'],
      category: [''],
      tag: [''],
      answers: this.fb.array([])
    },
      {
        validators: [this.validateCorrectAnswersCount]
      }
    );
    this.initAnswers();

    this.form.get('type')?.valueChanges.subscribe(() => {
      this.resetCorrectAnswers();
    });
  }

  get answers(): FormArray {
    return this.form.get('answers') as FormArray;
  }

  get categoryMode(): 'select' | 'input' {
    return this.form.get('categoryMode')?.value;
  }

  validateCorrectAnswersCount = (control: AbstractControl): ValidationErrors | null => {
    const form = control as FormGroup;
    const type = form.get('type')?.value;
    const answers = form.get('answers') as FormArray;
    if (type !== 'MULTIPLA') {
      return null;
    }
    const correctAnswersCount = answers.controls.filter(answer => {
      return answer.get('isCorrect')?.value === true;
    }).length;
    const answersCount = answers.controls.length;
    const isValid =
      correctAnswersCount >= 2 &&
      correctAnswersCount < answersCount;
    if (isValid) {
      return null;
    }
    return {
      validateCorrectAnswersCount: {
        valid: false,
        actual: correctAnswersCount,
        min: 2,
        max: answersCount - 1
      }
    };
  };

  initAnswers() {
    ['A', 'B', 'C', 'D'].forEach(label => {
      this.answers.push(this.createAnswer(label));
    });
  }

  createAnswer(label: string): FormGroup {
    return this.fb.group({
      label: [label],
      inputAnswer: ['', Validators.required],
      isCorrect: [false]
    });
  }

  addAnswer() {
    const label = String.fromCharCode(65 + this.answers.length);
    this.answers.push(this.createAnswer(label));
    this.form.updateValueAndValidity();
  }

  removeAnswer(index: number) {
    this.answers.removeAt(index);
    this.form.updateValueAndValidity();
  }

  setCorrect(index: number) {
    this.answers.controls.forEach((ctrl, i) => {
      ctrl.get('isCorrect')?.setValue(i === index);
    });
    this.form.updateValueAndValidity();
  }

  //emitEvent: false -> serve per evitare di scatenare eventi inutili per ogni singola risposta
  resetCorrectAnswers(): void {
    this.answers.controls.forEach(answer => {
      answer.get('isCorrect')?.setValue(false, { emitEvent: false });
    });
    this.form.updateValueAndValidity();
  }

  submit() {
    this.form.updateValueAndValidity();
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    //console.log(this.form.value);
    const { categoryMode, ...payload } = this.form.getRawValue();
    this.questionService.createQuestion(payload).subscribe();
  }

}
