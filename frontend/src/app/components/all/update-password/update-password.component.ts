import {Component, ViewChild} from '@angular/core';
import {AbstractControl, FormControl, FormGroup, NgForm, ValidatorFn, Validators} from "@angular/forms";

@Component({
  selector: 'app-update-password',
  templateUrl: './update-password.component.html',
  styleUrls: ['./update-password.component.scss']
})
export class UpdatePasswordComponent {
  @ViewChild('formDirective') private formDirective: NgForm | undefined;
  passwordForm = new FormGroup({
    id: new FormControl(`id`, Validators.required),
    currentPassword: new FormControl(``, Validators.required),
    newPassword: new FormControl(``, [
      Validators.required,
      this.passwordValidator()
    ]),
    newPasswordAgain: new FormControl(``, Validators.required),
  });

  constructor() {
    this.passwordForm.setValidators(this.passwordMatchValidator());
  }

  resetAll(){
    if (this.formDirective != undefined){
      this.formDirective.resetForm();
    }
    this.passwordForm.reset();
  }

  private passwordMatchValidator(): ValidatorFn {
    return (group: AbstractControl): { [key: string]: any } | null => {
      const newPassword = group.get('newPassword')?.value;
      const newPasswordAgain = group.get('newPasswordAgain')?.value;

      if (newPassword !== newPasswordAgain) {
        return { passwordMismatch: true };
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
}
