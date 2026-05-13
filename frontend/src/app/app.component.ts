import { Component } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { NavbarComponent } from "./layout/navbar/navbar.component";
import { SidenavComponent } from './layout/sidenav/sidenav.component';
import { filter } from 'rxjs';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavbarComponent, SidenavComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

  title = 'superquizzettone';
  isLoginOrRegister = false;

  private readonly publicRoutes = [
    '/login',
    '/register',
    '/auth/login',
    '/auth/register'
  ];

  constructor(private router: Router) {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        const currentUrl = event.urlAfterRedirects;

        this.isLoginOrRegister = this.publicRoutes.some(route =>
          currentUrl.startsWith(route)
        );
      });
  }

  isOpen = false;

  toggleSidenav() {
    this.isOpen = !this.isOpen;
  }

  closeSidenav() {
    this.isOpen = false;
  }


}
