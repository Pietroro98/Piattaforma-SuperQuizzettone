import { Component } from '@angular/core';
import { AuthService } from '../../core/service/auth.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-homepage',
  imports: [RouterLink],
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.scss'
})
export class HomepageComponent {

  constructor (private authService: AuthService){}


  prova() {
    this.authService.getCurrentUserNameAndLastName().subscribe(
      {next:
         (data => {
          console.log(data);
         }),
         error:(err =>{
          console.log(err);
         }
         )
      }
    );
  }
}
