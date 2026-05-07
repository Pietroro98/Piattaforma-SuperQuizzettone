import { Component, inject, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { QuestionService } from '../../core/service/question.service';
import { Router } from '@angular/router';
import { Category } from '../../core/models/quiz.model';
import { Answer } from '../../core/models/question.model';

@Component({
  selector: 'app-question-creator',
  imports: [ReactiveFormsModule],
  templateUrl: './question-creator.component.html',
  styleUrl: './question-creator.component.scss'
})
export class QuestionCreatorComponent implements OnInit {

  form: FormGroup;
  categories!: Category[];
  private readonly route = inject(Router);
  private readonly questionService = inject(QuestionService);

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      description: ['', Validators.required],
      type: ['SINGOLA'],
      categoryMode: ['select'],
      category: [null],
      categoryInput: [''],
      tag: [''],
      answers: this.fb.array([])
    },
      {
        validators: [this.validateCorrectAnswersCount]
      }
    );
    this.initDefaultAnswers();

    this.form.get('type')?.valueChanges.subscribe(type => {
      if (type === 'VEROFALSO') {
        this.initTrueFalseAnswers();
      } else {
        this.initDefaultAnswers();
      }

      this.form.updateValueAndValidity();
    });

    this.form.get('categoryMode')?.valueChanges.subscribe(() => {
      this.form.get('category')?.reset();
    });
  }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.questionService.getAllCategory().subscribe({
      next: (categories: Category[]) => {
        this.categories = categories;
      },
      error: (err) => {
        console.error("Errore nel caricamento delle categorie", err);
      }
    })
  }

  get answers(): FormArray {
    return this.form.get('answers') as FormArray;
  }

  get categoryMode(): 'select' | 'input' {
    return this.form.get('categoryMode')?.value;
  }

  get questionType(): string {
    return this.form.get('type')?.value;
  }

  get isSingle(): boolean {
    return this.questionType === 'SINGOLA';
  }

  get isMultiple(): boolean {
    return this.questionType === 'MULTIPLA';
  }

  get isTrueFalse(): boolean {
    return this.questionType === 'VEROFALSO';
  }

  validateCorrectAnswersCount = (control: AbstractControl): ValidationErrors | null => {
    const form = control as FormGroup;
    const type = form.get('type')?.value;
    const answers = form.get('answers') as FormArray;
    if (type !== 'MULTIPLA') {
      return null;
    }
    const correctAnswersCount = answers.controls.filter(answer => {
      return answer.get('correct')?.value === true;
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

  initDefaultAnswers(): void {
    this.answers.clear();
    ['A', 'B', 'C', 'D'].forEach(label => {
      this.answers.push(this.createAnswer(label));
    });
  }

  initTrueFalseAnswers(): void {
    this.answers.clear();
    this.answers.push(
      this.fb.group({
        label: ['A'],
        description: ['Vero', Validators.required],
        correct: [false]
      })
    );
    this.answers.push(
      this.fb.group({
        label: ['B'],
        description: ['Falso', Validators.required],
        correct: [false]
      })
    );
  }

  createAnswer(label: string): FormGroup {
    return this.fb.group({
      label: [label],
      description: ['', Validators.required],
      correct: [false]
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
      ctrl.get('correct')?.setValue(i === index);
    });
    this.form.updateValueAndValidity();
  }

  //emitEvent: false -> serve per evitare di scatenare eventi inutili per ogni singola risposta
  resetCorrectAnswers(): void {
    this.answers.controls.forEach(answer => {
      answer.get('correct')?.setValue(false, { emitEvent: false });
    });
    this.form.updateValueAndValidity();
  }


  submit(action: 'BOZZA' | 'CONFERMA'): void {
    this.form.updateValueAndValidity();
    if (action === 'CONFERMA' && this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const {
      categoryMode,
      categoryInput,
      ...rawPayload
    } = this.form.getRawValue();
    const category =
      categoryMode === 'select'
        ? rawPayload.category
        : { name: categoryInput };
    const payload = {
      ...rawPayload,
      category,
      ...(action === 'BOZZA' && {
        status: 'DRAFT'
      }),
      answers: rawPayload.answers.map((answer: any) => {
        const { label, ...cleanedAnswer } = answer;
        return cleanedAnswer;
      })
    };
    if (action === 'CONFERMA') {
      this.questionService.createQuestion(payload).subscribe({
        next: data => {
          console.log('Domanda creata', data);
        },
        error: err => {
          console.log('errore', err);
        }
      });
    }
    if (action === 'BOZZA') {
      this.questionService.saveDraft(payload).subscribe({
        next: data => {
          console.log('Domanda creata', data);
        },
        error: err => {
          console.log('errore', err);
        }
      });
    }
  }

}
