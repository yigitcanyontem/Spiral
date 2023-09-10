import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/authentication/login/login.component';
import { RegisterComponent } from './components/authentication/register/register.component';
import { UserProfileComponent } from './components/user/user-profile/user-profile.component';
import { MovieComponent } from './components/entertainment/movie/movie.component';
import { ShowComponent } from './components/entertainment/show/show.component';
import { GameComponent } from './components/entertainment/game/game.component';
import { AlbumComponent } from './components/entertainment/album/album.component';
import { BookComponent } from './components/entertainment/book/book.component';
import { UserUpdateComponent } from './components/user/user-update/user-update.component';
import { UserReviewsComponent } from './components/user/user-reviews/user-reviews.component';
import { MainpageWrapperComponent } from './components/all/mainpage-wrapper/mainpage-wrapper.component';
import { SearchResultsComponent } from './components/all/search-results/search-results.component';

const routes: Routes = [
  { path: 'profile/:userid', component: UserProfileComponent },
  { path: '', component: MainpageWrapperComponent },
  { path: 'reviews/user/:userid', component: UserReviewsComponent },
  { path: 'update', component: UserUpdateComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: RegisterComponent },
  { path: 'movie/:id', component: MovieComponent },
  { path: 'show/:id', component: ShowComponent },
  { path: 'game/:id', component: GameComponent },
  { path: 'album/:id', component: AlbumComponent },
  { path: 'book/:id', component: BookComponent },
  { path: 'search/:query', component: SearchResultsComponent },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { scrollPositionRestoration: 'enabled' }),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
