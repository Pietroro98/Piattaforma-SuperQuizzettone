import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService, LoginPayload } from '../../core/service/auth.service';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  private fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  showPassword: boolean = false;

  loginForm = this.fb.group({
    username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(15)]],
    password: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(15)]],
  });

  showHide() {
    this.showPassword = !this.showPassword;
  }

  onSubmit() {
    console.log("SONO QUI");
 /*    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    } */
    const payload = this.loginForm.value as LoginPayload;
    console.log('Dati da inviare:', payload);
    this.authService.login(payload).subscribe({
      next: (response) => {
        this.loginForm.reset();
        this.router.navigateByUrl('/home');
      },
      error: (err) => {
        console.error('Errore registrazione', err);
      }
    });
  }

}
