import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./components/authentication/login/login.component";
import {RegisterComponent} from "./components/authentication/register/register.component";
import {UserProfileComponent} from "./components/user/user-profile/user-profile.component";
import {MovieComponent} from "./components/entertainment/movie/movie.component";
import {ShowComponent} from "./components/entertainment/show/show.component";
import {GameComponent} from "./components/entertainment/game/game.component";
import {AlbumComponent} from "./components/entertainment/album/album.component";
import {BookComponent} from "./components/entertainment/book/book.component";
import {UserUpdateComponent} from "./components/user/user-update/user-update.component";

const routes: Routes = [
  {path: 'profile/:userid', component: UserProfileComponent},
  {path: 'update', component: UserUpdateComponent},
  {path: 'login', component: LoginComponent},
  {path: 'signup', component: RegisterComponent},
  {path: 'movie/:id', component: MovieComponent},
  {path: 'show/:id', component: ShowComponent},
  {path: 'game/:id', component: GameComponent},
  {path: 'album/:id', component: AlbumComponent},
  {path: 'book/:id', component: BookComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
