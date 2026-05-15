import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/service/auth.service';

@Component({
  selector: 'app-homepage',
  imports: [RouterLink],
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.scss',
})
export class HomepageComponent {
  constructor(private authService: AuthService) {}

  quizzes = [
    {
      title: 'Quiz sicurezza informatica',
      description: 'Verifica le tue conoscenze su phishing, password e buone pratiche.',
      duration: '15 min',
    },
    {
      title: 'Quiz cultura generale',
      description: 'Domande rapide per allenare memoria, logica e conoscenze.',
      duration: '10 min',
    },
    {
      title: 'Quiz aziendale',
      description: 'Test utile per procedure interne, policy e formazione.',
      duration: '20 min',
    },
  ];
}