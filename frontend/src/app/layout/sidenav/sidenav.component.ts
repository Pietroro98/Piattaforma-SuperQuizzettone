import { Component, EventEmitter, Input, Output } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../core/service/auth.service';
import { NgIf } from '@angular/common';

interface MenuItem {
  label: string;
  route: string;
  roles?: string[];
}

@Component({
  selector: 'app-sidenav',
  imports: [RouterLink, RouterLinkActive, NgIf],
  templateUrl: './sidenav.component.html',
  styleUrl: './sidenav.component.scss',
})
export class SidenavComponent {
  @Input() isOpen = false;
  @Output() close = new EventEmitter<void>();

  constructor(private authService: AuthService) {}

  menuItems: MenuItem[] = [
    {
      label: 'Home',
      route: '/home',
    },
    {
      label: 'Play',
      route: '/play',
      roles: ['ROLE_PLAYER'],
    },
    {
      label: 'Profilo',
      route: '/profile',
    },
    {
      label: 'Backoffice',
      route: '/backoffice',
      roles: ['ROLE_ADMINISTRATOR', 'ROLE_REVIEWER','ROLE_ERITER'],
    },
  ];

  onClose(): void {
    this.close.emit();
  }

  canShowItem(item: MenuItem): boolean {
    if (!item.roles || item.roles.length === 0) {
      return true;
    }

    return this.authService.hasAnyRole(item.roles);
  }

  logout(): void {
    this.authService.logout();
    window.location.href = '/login';
  }
}
