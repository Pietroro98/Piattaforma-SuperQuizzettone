import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-question-creator',
  imports: [ReactiveFormsModule],
  templateUrl: './question-creator.component.html',
  styleUrl: './question-creator.component.scss'
})
export class QuestionCreatorComponent {

  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      inputQuestion: ['', Validators.required],
      type: ['SINGOLA'],
      categoryMode: ['select'],
      category: [''],
      tag: [''],
      answers: this.fb.array([])
    });
    this.initAnswers();
  }

  get answers(): FormArray {
    return this.form.get('answers') as FormArray;
  }

  get categoryMode(): 'select' | 'add' {
  return this.form.get('categoryMode')?.value;
}

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
  }

  removeAnswer(index: number) {
    this.answers.removeAt(index);
  }

  setCorrect(index: number) {
    this.answers.controls.forEach((ctrl, i) => {
      ctrl.get('isCorrect')?.setValue(i === index);
    });
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    console.log(this.form.value);
  }

}
