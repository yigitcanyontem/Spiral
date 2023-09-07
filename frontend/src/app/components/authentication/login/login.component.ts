import { Component } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { AuthenticationService } from '../../../services/authentication.service';
import { Router } from '@angular/router';
import { AuthenticationRequest } from '../../../models/authentication-request';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  providers: [MessageService],
})
export class LoginComponent {
  frm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthenticationService,
    private messageService: MessageService,
  ) {
    this.frm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(1)]],
    });
  }

  submitForm() {
    this.authService
      .onLogin({
        username: this.username?.value,
        password: this.password?.value,
      } as AuthenticationRequest)
      .subscribe(
        (bool) => {
          if (bool) {
            this.router
              .navigate([`profile/${localStorage.getItem('forum_user_id')}`])
              .then(() => {
                window.location.reload();
              });
          } else {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Wrong username or password',
            });
          }
        },
        (error) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Wrong username or password',
          });
        },
      );
  }

  get username() {
    return this.frm.get('username');
  }
  get password() {
    return this.frm.get('password');
  }
}
