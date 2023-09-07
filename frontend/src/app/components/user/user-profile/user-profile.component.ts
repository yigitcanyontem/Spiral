import { Component, HostListener, OnInit } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { UserDTO } from '../../../models/user-dto';
import { Movie } from '../../../models/movie';
import { Show } from '../../../models/show';
import { Album } from '../../../models/album';
import { Book } from '../../../models/book';
import { Game } from '../../../models/game';
import { SocialMediaDTO } from '../../../models/socialmedia-dto';
import { Description } from '../../../models/description';
import { forkJoin } from 'rxjs';
import { EntertainmentService } from '../../../services/entertainment.service';
import { FavGame } from '../../../models/favGame';
import { FavGameDto } from '../../../models/fav-game-dto';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss'],
})
export class UserProfileComponent implements OnInit {
  userid = parseInt(
    <string>this.activatedRoute.snapshot.paramMap.get('userid'),
  );
  responsiveOptions: any[] | undefined;

  isLoaded = false;
  user!: UserDTO;
  movies!: Movie[];
  shows!: Show[];
  books!: Book[];
  games!: FavGameDto[];
  albums!: Album[];

  socialMedia!: SocialMediaDTO;
  images!: string;
  description!: Description;

  constructor(
    private activatedRoute: ActivatedRoute,
    private userService: UserService,
    private router: Router,
    private entertainmentService: EntertainmentService,
  ) {
    this.responsiveOptions = [
      {
        breakpoint: '1199px',
        numVisible: 1,
        numScroll: 1,
      },
      {
        breakpoint: '991px',
        numVisible: 2,
        numScroll: 1,
      },
      {
        breakpoint: '767px',
        numVisible: 1,
        numScroll: 1,
      },
    ];
  }

  ngOnInit(): void {
    const getUser$ = this.userService.getUser(this.userid);
    const getSocialMedia$ = this.userService.getCustomerSocialMedia(
      this.userid,
    );
    const getDescription$ = this.userService.getCustomerDescription(
      this.userid,
    );
    const getFavMovies$ = this.userService.getFavMovies(this.userid);
    const getFavShows$ = this.userService.getFavShows(this.userid);
    const getFavAlbums$ = this.userService.getFavAlbums(this.userid);
    const getFavBooks$ = this.userService.getFavBooks(this.userid);
    const getFavGames$ = this.userService.getFavGames(this.userid);
    const getImage$ = this.userService.getImage(this.userid);

    forkJoin({
      user: getUser$,
      socialMedia: getSocialMedia$,
      description: getDescription$,
      movies: getFavMovies$,
      shows: getFavShows$,
      books: getFavBooks$,
      games: getFavGames$,
      albums: getFavAlbums$,
      images: getImage$,
    }).subscribe(
      (results: {
        user: UserDTO;
        socialMedia: SocialMediaDTO;
        description: Description;
        movies: Movie[];
        shows: Show[];
        books: Book[];
        games: FavGameDto[];
        albums: Album[];
        images: Blob;
      }) => {
        this.user = results.user;
        this.socialMedia = results.socialMedia;
        this.description = results.description;
        this.movies = results.movies;
        this.shows = results.shows;
        this.books = results.books;
        this.games = results.games;
        this.albums = results.albums;
        this.images = URL.createObjectURL(results.images);
        this.isLoaded = true;
        this.getSingleResultsForGames();
      },
      (error) => {
        // Handle errors
      },
    );
  }

  getSingleResultsForGames() {
    this.games.forEach((value) => {
      this.entertainmentService.onGetGame(value.gameid).subscribe();
    });
  }

  scrollTop() {
    window.scroll(0, 0);
  }

  protected readonly onscroll = onscroll;
}
