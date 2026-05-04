import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService, RegisterPayload } from '../../core/service/auth.service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  private fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  showPassword: boolean = false;

  showHide() {
    this.showPassword = !this.showPassword;
  }

  registerForm = this.fb.group({
    username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(15)]],
    password: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(15)]],
    name: ['', [Validators.required]],
    surname: ['', [Validators.required]]
  });

  onSubmit() {
 /*    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    } */
    const payload = this.registerForm.value as RegisterPayload;
    console.log('Dati da inviare:', payload);
    this.authService.register(payload).subscribe({
      next: (response) => {
        //   this.toastService.push('success', 'Registrazione completata con successo.');
        this.registerForm.reset();
        this.router.navigateByUrl('/login');
      },
      error: (err) => {
        console.error('Errore registrazione', err);
      }
    });
  }

}
