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

  questions: Question[] = [
    {
    id: 1,
    description: "Qual è la capitale della Francia?",
    tag: "Geografia",
    category: { 
      id: 1, 
      name: "Cultura Generale", 
      description: "Domande di vario tipo sulla cultura" // <--- AGGIUNTO
    }, 
    status: "IN_REVIEW",
    answers: [
      { id: 10, description: "Parigi", isCorrect: true },
      { id: 11, description: "Lione", isCorrect: false }
    ]
  },
  {
    id: 2,
    description: "Quale linguaggio usa Angular?",
    tag: "Programmazione",
    category: { 
      id: 2, 
      name: "Informatica", 
      description: "Sviluppo web e software" // <--- AGGIUNTO
    },
    status: "IN_REVIEW",
    answers: [
      { id: 20, description: "Java", isCorrect: false },
      { id: 21, description: "TypeScript", isCorrect: true }
    ]
  }
  ];

  constructor(private questionService: QuestionService) { }

  @Input() description: string= '';

  ngOnInit(): void {
    this.caricaDomandeReviewer();
  }

  caricaDomandeReviewer(){
    this.questionService.getQuestionsInReview().subscribe({
      next: (data) => {
        this.questions = data;
        console.log('Domande ricevute correttamente', data);
      },
      error: (err) => {
        console.log('Errore nel caricamento dei dati', err);
      }
    });
  }



}
