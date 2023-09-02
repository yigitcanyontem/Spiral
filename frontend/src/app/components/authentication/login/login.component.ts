import { Component } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from "../../../services/authentication.service";
import {Router} from "@angular/router";
import {AuthenticationRequest} from "../../../models/authentication-request";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  frm: FormGroup;

  constructor(private formBuilder: FormBuilder,private router:Router, private authService:AuthenticationService ) {
    this.frm = this.formBuilder.group({
      username: ["", [Validators.required]],
      password: ["", [Validators.required, Validators.minLength(1)]],
    })
  }

  submitForm(){
    this.authService.onLogin(
      {
        username: this.username?.value,
        password: this.password?.value
      }as AuthenticationRequest)
    .subscribe((bool) => {
      if (bool){
        this.router.navigate([`profile/${localStorage.getItem('forum_user_id')}`])
          .then(() => {
            window.location.reload();
          });
      }
    });
  }

  get username() {
    return this.frm.get("username");
  }
  get password() {
    return this.frm.get("password");
  }
}
