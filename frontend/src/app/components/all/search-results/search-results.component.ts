import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { SearchService } from '../../../services/search.service';
import { forkJoin, map } from 'rxjs';
import { Movie } from '../../../models/movie';
import { Show } from '../../../models/show';
import { Users } from '../../../models/users';
import { GameSearchDTO } from '../../../models/game-search-dto';
import { Album } from '../../../models/album';
import { Book } from '../../../models/book';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-search-results',
  templateUrl: './search-results.component.html',
  styleUrls: ['./search-results.component.scss'],
})
export class SearchResultsComponent implements OnInit {
  isLoaded = false;
  query = this.activatedRoute.snapshot.params['query'];

  shows: Show[] = [];
  movies: Movie[] = [];
  books: Book[] = [];
  albums: Album[] = [];
  games: GameSearchDTO[] = [];
  users: Users[] = [];

  constructor(
    private activatedRoute: ActivatedRoute,
    private searchService: SearchService,
    private userService: UserService,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: Params) => {
      this.query = params['query'];
      this.isLoaded = false;
      this.getSearchResults();
    });
  }

  getSearchResults() {
    const getMovies$ = this.searchService.getMovieSearchResults(this.query);
    const getBooks$ = this.searchService.getBookSearchResults(this.query);
    const getShows$ = this.searchService.getShowSearchResults(this.query);
    const getAlbums$ = this.searchService.getAlbumSearchResults(this.query);
    const getGames$ = this.searchService.getGameSearchResults(this.query);
    const getUsers$ = this.searchService.getUserSearchResults(this.query);
    forkJoin({
      movies: getMovies$,
      books: getBooks$,
      shows: getShows$,
      albums: getAlbums$,
      games: getGames$,
      users: getUsers$,
    }).subscribe(
      (results: {
        movies: Movie[];
        books: Book[];
        shows: Show[];
        albums: Album[];
        games: GameSearchDTO[];
        users: Users[];
      }) => {
        this.users = results.users;
        this.getUserProfilePictures();
        this.movies = results.movies;
        this.books = results.books;
        this.shows = results.shows;
        this.albums = results.albums;
        this.games = results.games;

        this.isLoaded = true;
      },
      (error) => {
        // Handle errors
      },
    );
  }
  userProfilePicturesMap: Map<number, string> = new Map<number, string>();

  getUserProfilePictures(): void {
    forkJoin(
      this.users.map((user) =>
        this.userService
          .getImage(user.id)
          .pipe(map((image) => ({ userId: user.id, image }))),
      ),
    )
      .pipe(
        map((results) => {
          results.forEach((result) => {
            this.userProfilePicturesMap.set(
              result.userId,
              URL.createObjectURL(result.image),
            );
          });
        }),
      )
      .subscribe(() => {});
  }
}
