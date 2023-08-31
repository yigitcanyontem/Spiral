import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ReportComponent} from "./components/all/report/report.component";
import {UpdatePasswordComponent} from "./components/all/update-password/update-password.component";
import {MainpageWrapperComponent} from "./components/all/mainpage-wrapper/mainpage-wrapper.component";
import {LoginComponent} from "./components/all/login/login.component";
import {AllUsersComponent} from "./components/admin/all-users/all-users.component";
import {RegisterComponent} from "./components/all/register/register.component";
import {UserProfileComponent} from "./components/user-profile/user-profile.component";

const routes: Routes = [
  {path: 'profile/:userid', component: UserProfileComponent},
  {path: 'issue', component: ReportComponent},
  {path: 'password', component: UpdatePasswordComponent},
  {path: 'login', component: LoginComponent},
  {path: 'signup', component: RegisterComponent},
  {path: 'all', component: AllUsersComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
