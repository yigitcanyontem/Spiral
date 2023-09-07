import { Injectable } from '@angular/core';
import { environment } from '../environment/environment';
import { Show } from '../models/show';
import { Album } from '../models/album';
import { Observable } from 'rxjs';
import { Book } from '../models/book';
import { Users } from '../models/users';
import { Movie } from '../models/movie';
import { GameSearchDTO } from '../models/game-search-dto';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class SearchService {
  private apiUrl = environment.apiUrl;
  private backendUrl = `${this.apiUrl}/search`;

  constructor(private http: HttpClient) {}

  getShowSearchResults(title: string): Observable<Show[]> {
    const url = `${this.backendUrl}/show/${title}`;
    return this.http.get<Show[]>(url);
  }

  getAlbumSearchResults(title: string): Observable<Album[]> {
    const url = `${this.backendUrl}/album/${title}`;
    return this.http.get<Album[]>(url);
  }

  getBookSearchResults(title: string): Observable<Book[]> {
    const url = `${this.backendUrl}/book/${title}`;
    return this.http.get<Book[]>(url);
  }

  getMovieSearchResults(title: string): Observable<Movie[]> {
    const url = `${this.backendUrl}/movie/${title}`;
    return this.http.get<Movie[]>(url);
  }

  getUserSearchResults(username: string): Observable<Users[]> {
    const url = `${this.backendUrl}/user/${username}`;
    return this.http.get<Users[]>(url);
  }

  getGameSearchResults(title: string): Observable<GameSearchDTO[]> {
    const url = `${this.backendUrl}/game/${title}`;
    return this.http.get<GameSearchDTO[]>(url);
  }
}
