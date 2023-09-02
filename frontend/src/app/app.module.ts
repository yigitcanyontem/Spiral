import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/layout/header/header.component';
import { FooterComponent } from './components/layout/footer/footer.component';
import { LoginComponent } from './components/authentication/login/login.component';
import { MainpageWrapperComponent } from './components/all/mainpage-wrapper/mainpage-wrapper.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import { RegisterComponent } from './components/authentication/register/register.component';
import { UserProfileComponent } from './components/user/user-profile/user-profile.component';
import { UserReviewsComponent } from './components/user/user-reviews/user-reviews.component';
import { MovieComponent } from './components/entertainment/movie/movie.component';
import { BookComponent } from './components/entertainment/book/book.component';
import { GameComponent } from './components/entertainment/game/game.component';
import { ShowComponent } from './components/entertainment/show/show.component';
import { AlbumComponent } from './components/entertainment/album/album.component';
import { SpinnerComponent } from './components/layout/spinner/spinner.component';
import {CarouselModule} from "primeng/carousel";
import {TagModule} from "primeng/tag";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {PasswordModule} from "primeng/password";
import {MessageModule} from "primeng/message";
import {CalendarModule} from "primeng/calendar";
import {DropdownModule} from "primeng/dropdown";
import {RatingModule} from "primeng/rating";
import {DividerModule} from "primeng/divider";
import { UserUpdateComponent } from './components/user/user-update/user-update.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    LoginComponent,
    MainpageWrapperComponent,
    RegisterComponent,
    UserProfileComponent,
    UserReviewsComponent,
    MovieComponent,
    BookComponent,
    GameComponent,
    ShowComponent,
    AlbumComponent,
    SpinnerComponent,
    UserUpdateComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    CarouselModule,
    TagModule,
    ButtonModule,
    InputTextModule,
    PasswordModule,
    MessageModule,
    CalendarModule,
    DropdownModule,
    RatingModule,
    DividerModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
