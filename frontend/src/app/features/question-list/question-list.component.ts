import { Component, Input, OnInit } from '@angular/core';
import { Accordion } from '../accordion/accordion';
import { Question } from '../../core/models/question.model';
import { QuestionService } from '../../core/service/question.service';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/service/auth.service';

@Component({
  selector: 'app-question-list',
  imports: [Accordion, CommonModule],
  templateUrl: './question-list.component.html',
  styleUrl: './question-list.component.scss',
})
export class QuestionListComponent implements OnInit{

  questions: Question[] = [];

  constructor(private questionService: QuestionService, public authService: AuthService) { }

  @Input() description: string= '';

  ngOnInit(): void {
    this.caricaDomandeReviewer();
  }

  caricaDomandeReviewer(){
    this.questionService.getQuestionsInReview().subscribe({
      next: (response : any) => {
        this.questions = response.data;
        console.log('Domande ricevute correttamente', this.questions);
      },
      error: (err) => {
        console.log('Errore nel caricamento dei dati', err);
      }
    });
  }


 get canAddQuestion(): boolean {
  const ruoli = this.authService.userRole();
  
  
  const isW = this.authService.isWriter();
  const isP = this.authService.isPlayer();
  const isA = this.authService.isAdmin();
  
  
  return isW || isP || isA;
}

get canAddDetails(): boolean {
  const ruoli = this.authService.userRole();
  const isR = this.authService.isReviewer();
  const isA = this.authService.isAdmin();

  return isR || isA;
}


}
