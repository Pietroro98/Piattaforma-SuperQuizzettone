import { Component, Input, OnInit } from '@angular/core';
import { Accordion } from '../accordion/accordion';
import { Question } from '../../core/models/question.model';
import { QuestionService } from '../../core/service/question.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-question-list',
  imports: [Accordion, CommonModule],
  templateUrl: './question-list.component.html',
  styleUrl: './question-list.component.scss',
})
export class QuestionListComponent implements OnInit{

  questions: Question[] = [];

  constructor(private questionService: QuestionService) { }

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



}
