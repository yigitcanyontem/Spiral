import {Component, HostListener, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {UserDTO} from "../../models/user-dto";
import {Movie} from "../../models/movie";
import {Show} from "../../models/show";
import {Album} from "../../models/album";
import {Book} from "../../models/book";
import {Game} from "../../models/game";
import {SocialMediaDTO} from "../../models/socialmedia-dto";
import {Description} from "../../models/description";
import {forkJoin} from "rxjs";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit{
  userid =  parseInt(<string>this.activatedRoute.snapshot.paramMap.get('userid'));

  movie_index = 0;
  show_index = 0;
  game_index = 0;
  album_index = 0;
  book_index = 0;

  movie_count = 6;
  show_count = 6;
  game_count = 6;
  album_count = 6;
  book_count = 6;

  isLoaded = false;
  user!: UserDTO ;
  movies!: Movie[] ;
  shows!: Show[] ;
  books!: Book[] ;
  games!: Game[] ;
  albums!: Album[] ;
  socialMedia!: SocialMediaDTO ;
  images!: string ;
  description!: Description ;

  constructor(private activatedRoute: ActivatedRoute,private userService: UserService, private router:Router) {

  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.onWindowResize(event.target.innerWidth);
  }

  onWindowResize(windowWidth: number) {
    if(windowWidth>991){
      this.counter()
    }
    if(windowWidth<991){
      this.movie_count = 2;
      this.show_count = 2;
      this.game_count = 2;
      this.album_count = 2;
      this.book_count = 2;
    }
  }

  ngOnInit(): void {
    if(window.innerWidth<991){
      this.movie_count = 2;
      this.show_count = 2;
      this.game_count = 2;
      this.album_count = 2;
      this.book_count = 2;
    }
    const getUser$ = this.userService.getUser((this.userid));
    const getSocialMedia$ = this.userService.getCustomerSocialMedia((this.userid));
    const getDescription$ = this.userService.getCustomerDescription((this.userid));
    const getFavMovies$ = this.userService.getFavMovies((this.userid));
    const getFavShows$ = this.userService.getFavShows((this.userid));
    const getFavAlbums$ = this.userService.getFavAlbums((this.userid));
    const getFavBooks$ = this.userService.getFavBooks((this.userid));
    const getFavGames$ = this.userService.getFavGames((this.userid));
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
      images: getImage$
    }).subscribe(
      (results: {
        user: UserDTO,
        socialMedia: SocialMediaDTO,
        description: Description,
        movies: Movie[],
        shows: Show[],
        books: Book[],
        games: Game[],
        albums: Album[],
        images: Blob
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
        this.counter()
        this.isLoaded = true;
      },
      error => {
        // Handle errors
      }
    );

  }

  counter(){
    if (this.movies.length < 6){
      this.movie_count = this.movies.length;
    }else {
      this.movie_count = 6;
    }

    if (this.shows.length < 6){
      this.show_count = this.shows.length;
    }else {
      this.show_count = 6;
    }

    if (this.games.length < 6){
      this.game_count = this.games.length;
    }else {
      this.game_count = 6;
    }

    if (this.albums.length < 6){
      this.album_count = this.albums.length;
    }else {
      this.album_count = 6;
    }

    if (this.books.length < 6){
      this.book_count = this.books.length;
    }else {
      this.book_count = 6;
    }
  }

}
