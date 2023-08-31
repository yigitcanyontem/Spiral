import { NgDompurifySanitizer } from "@tinkoff/ng-dompurify";
import {
    TuiRootModule,
    TuiDialogModule,
    TuiAlertModule,
    TUI_SANITIZER,
    TuiErrorModule,
    TuiButtonModule,
    TuiHintModule,
    TuiTooltipModule,
    TuiTextfieldControllerModule,
    TuiDataListModule,
    TuiLoaderModule,
    TuiDropdownModule, TuiModeModule
} from "@taiga-ui/core";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/layout/header/header.component';
import { FooterComponent } from './components/layout/footer/footer.component';
import { UpdatePasswordComponent } from './components/all/update-password/update-password.component';
import { ReportComponent } from './components/all/report/report.component';
import { StudentMainpageComponent } from './components/student/student-mainpage/student-mainpage.component';
import { LoginComponent } from './components/all/login/login.component';
import { AllUsersComponent } from './components/admin/all-users/all-users.component';
import { MainpageWrapperComponent } from './components/all/mainpage-wrapper/mainpage-wrapper.component';
import {TuiTableModule} from "@taiga-ui/addon-table";
import {
  TuiCarouselModule,
  TuiCheckboxLabeledModule,
  TuiDataListWrapperModule, TuiFieldErrorPipeModule, TuiInputDateModule,
  TuiInputModule,
  TuiInputPasswordModule,
  TuiRadioBlockModule, TuiSelectModule, TuiTextareaModule
} from "@taiga-ui/kit";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import { RegisterComponent } from './components/all/register/register.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { UserReviewsComponent } from './components/user-reviews/user-reviews.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    UpdatePasswordComponent,
    ReportComponent,
    StudentMainpageComponent,
    LoginComponent,
    AllUsersComponent,
    MainpageWrapperComponent,
    RegisterComponent,
    UserProfileComponent,
    UserReviewsComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    TuiRootModule,
    TuiDialogModule,
    TuiAlertModule,
    TuiTableModule,
    TuiInputModule,
    TuiDataListWrapperModule,
    FormsModule,
    TuiRadioBlockModule,
    TuiCheckboxLabeledModule,
    TuiErrorModule,
    TuiInputPasswordModule,
    ReactiveFormsModule,
    TuiFieldErrorPipeModule,
    TuiButtonModule,
    TuiHintModule,
    TuiTooltipModule,
    TuiTextfieldControllerModule,
    TuiTextareaModule,
    TuiInputDateModule,
    TuiSelectModule,
    TuiDataListModule,
    TuiLoaderModule,
    TuiDropdownModule,
    TuiModeModule,
    TuiCarouselModule
  ],
  providers: [HttpClient,{provide: TUI_SANITIZER, useClass: NgDompurifySanitizer}],
  bootstrap: [AppComponent]
})
export class AppModule { }
