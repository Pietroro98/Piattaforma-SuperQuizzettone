import { Component, EventEmitter, Input, Output } from '@angular/core';
import { LayoutService } from '../layout.service';
import { NgIf } from '@angular/common';
import { RouterLink } from "@angular/router";

@Component({
  selector: 'app-navbar',
  imports: [NgIf, RouterLink],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
  providers: []
})
export class NavbarComponent {

  @Input() isOpen = false;
  @Output() toggle = new EventEmitter<void>();



  constructor(private readonly layoutSvc: LayoutService) {
    layoutSvc.collapsed$.subscribe(next => {
      this.isOpen = next;
    })
  }

  getInitials(firstName?: string, lastName?: string): string {
    if (!firstName && !lastName) return '';

    const first = firstName ? firstName.charAt(0) : '';
    const last = lastName ? lastName.charAt(0) : '';

    return (first + last).toUpperCase();
  }

  confirmLogout() {
    // da implementare
  }

  logout() {
    //da implementare
  }

  toggleSidenav() {
    this.toggle.emit(); // 🔥 fondamentale
  }
}
