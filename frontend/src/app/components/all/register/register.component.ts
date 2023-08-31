import { Component } from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../../services/authentication.service";
import {RegisterRequest} from "../../../models/register-request";
import {EntertainmentService} from "../../../services/entertainment.service";
import {Country} from "../../../models/country";
import {TuiContextWithImplicit, TuiDay, tuiPure, TuiStringHandler} from "@taiga-ui/cdk";

interface CountryDTO {
  readonly id: number;
  readonly name: string;
}
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  frm: FormGroup;
  countries: Country[] = [];
  maxDate: TuiDay = TuiDay.currentLocal();
  dateOfBirth: Date = new Date();
  selectedCountryId: number = 1;
  constructor(private formBuilder: FormBuilder,private router:Router, private authService:AuthenticationService) {
    authService.onGetCountries().subscribe(
      (response) => {
        this.countries = response;
      }
    )

    this.frm = this.formBuilder.group({
      firstName: ["", [Validators.required]],
      lastName: ["", [Validators.required]],
      date_of_birth: [new TuiDay(2000, 0, 1), [Validators.required]],
      country: [this.selectedCountryId, [Validators.required]],
      email: ["", [Validators.required,this.emailValidator()]],
      username: ["", [Validators.required]],
      password: ["", [Validators.required, this.passwordValidator()]],
    })

    // @ts-ignore
    this.frm.get('country').valueChanges.subscribe((selectedCountry) => {
      this.selectedCountryId = selectedCountry?.id;
      console.log(selectedCountry)
    });
  }

  @tuiPure
  stringify(
    items: readonly CountryDTO[],
  ): TuiStringHandler<TuiContextWithImplicit<number>> {
    const map = new Map(items.map(({id, name}) => [id, name] as [number, string]));

    return ({$implicit}: TuiContextWithImplicit<number>) => map.get($implicit) || '';
  }

  submitForm(){
    this.dateOfBirth = this.frm.get("date_of_birth")?.value.toLocalNativeDate();
    this.authService.onRegister(
      {
        firstName: this.firstName?.value,
        lastName: this.lastName?.value,
        date_of_birth: this.dateOfBirth,
        country: this.country?.value,
        email: this.email?.value,
        username: this.username?.value,
        password: this.password?.value
      }as RegisterRequest)
      .subscribe((bool) => {
        if (bool){
          window.location.reload()
        }
      });
  }


  private emailValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const value = control.value;

      if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value)) {
        return { invalidEmail: true };
      }

      return null;
    };
  }
  private passwordValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const value = control.value;

      if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}/.test(value)) {
        return { invalidPassword: true };
      }

      return null;
    };
  }






  get firstName() {
    return this.frm.get('firstName');
  }

  get lastName() {
    return this.frm.get('lastName');
  }

  get date_of_birth() {
    return this.frm.get('date_of_birth');
  }

  get country() {
    return this.frm.get('country');
  }

  get email() {
    return this.frm.get('email');
  }

  get username() {
    return this.frm.get('username');
  }

  get password() {
    return this.frm.get('password');
  }

  protected readonly event = event;
}
