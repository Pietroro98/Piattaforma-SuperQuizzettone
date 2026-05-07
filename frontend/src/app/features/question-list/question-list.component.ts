import { Component } from '@angular/core';
import { Accordion } from '../accordion/accordion';

@Component({
  selector: 'app-quiz',
  imports: [Accordion],
  templateUrl: './question-list.component.html',
  styleUrl: './question-list.component.scss',
})
export class QuestionListComponent {

  questions = [] 
}
