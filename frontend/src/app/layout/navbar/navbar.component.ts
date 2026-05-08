import { Component, EventEmitter, Input, Output } from '@angular/core';
import { LayoutService } from '../layout.service';
import { AsyncPipe, NgIf } from '@angular/common';
import { RouterLink } from "@angular/router";
import { AuthService } from '../../core/service/auth.service';
import { Observable } from 'rxjs';
import { User } from '../../core/models/user.model';

@Component({
  selector: 'app-navbar',
  imports: [NgIf, RouterLink, AsyncPipe],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
  providers: []
})
export class NavbarComponent {

  @Input() isOpen = false;
  @Output() toggle = new EventEmitter<void>();
  user$: Observable<Omit<User, 'password'> | null>;
  initials$: Observable<string | null>;

  constructor(private readonly layoutSvc: LayoutService, private authService: AuthService) {
    this.layoutSvc.collapsed$.subscribe(next => {
      this.isOpen = next;
    })
    this.user$ = this.authService.currentUser$;
    this.initials$ = this.authService.initials$;
  }

  getInitials(nome?: string, cognome?: string): string {
    if (!nome && !cognome) return '?';

    const first = nome ? nome.charAt(0) : '';
    const last = cognome ? cognome.charAt(0) : '';

    return (first + last).toUpperCase();
  }

  confirmLogout() {
    // da implementare
  }

  logout() {
    this.authService.logout();
  }

  toggleSidenav() {
    this.toggle.emit();
  }
}
