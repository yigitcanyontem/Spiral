import {Injectable} from '@angular/core';
import {environment} from "../environment/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, Observable, of} from "rxjs";
import {AssignModel} from "../models/assign-model";
import {Description} from "../models/description";
import {SocialMediaDTO} from "../models/socialmedia-dto";
import {Users} from "../models/users";
import {UserDTO} from "../models/user-dto";
import {Show} from "../models/show";
import {Album} from "../models/album";
import {Movie} from "../models/movie";
import {Book} from "../models/book";
import {Game} from "../models/game";
import {Resource} from "@angular/compiler-cli/src/ngtsc/metadata";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {
  }

  token = `Bearer ${localStorage.getItem('forum_access_token')}`;

  private backendUrl = `${this.apiUrl}/user`;

  getUser(id: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.backendUrl}/${id}`);
  }

  getCustomerSocialMedia(id: number): Observable<SocialMediaDTO> {
    return this.http.get<SocialMediaDTO>(`${this.backendUrl}/socialmedia/${id}`);
  }

  getCustomerDescription(id: number): Observable<Description> {
    return this.http.get<Description>(`${this.backendUrl}/description/${id}`);
  }

  updateCustomer(assignModel: AssignModel): Observable<string[]> {
    return this.http.patch<string[]>(`${this.backendUrl}/update`, assignModel, {
      headers: new HttpHeaders({
        'Authorization': this.token
      })
    });
  }

  deleteCustomer(usersid: number): Observable<any> {
    return this.http.delete<any>(`${this.backendUrl}/delete/${usersid}`, {
      headers: new HttpHeaders({
        'Authorization': this.token
      })
    });
  }

  getFavMovies(id: number): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.backendUrl}/favmovie/${id}`);
  }

  getFavShows(id: number): Observable<Show[]> {
    return this.http.get<Show[]>(`${this.backendUrl}/favshows/${id}`);
  }

  getFavAlbums(id: number): Observable<Album[]> {
    return this.http.get<Album[]>(`${this.backendUrl}/favalbums/${id}`);
  }

  getFavBooks(id: number): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.backendUrl}/favbooks/${id}`);
  }

  getFavGames(id: number): Observable<Game[]> {
    return this.http.get<Game[]>(`${this.backendUrl}/favgames/${id}`);
  }

  saveFavMovies(usersid: number, movieid: number): Observable<any> {
    return this.http.put<any>(`${this.backendUrl}/favmovie/${usersid}/${movieid}`, null, {
      headers: new HttpHeaders({
        'Authorization': this.token
      })
    });
  }

  saveFavShows(usersid: number, showid: number): Observable<any> {
    return this.http.put<any>(`${this.backendUrl}/favshows/${usersid}/${showid}`, null, {
      headers: new HttpHeaders({
        'Authorization': this.token
      })
    });
  }

  saveFavAlbums(usersid: number, albumid: string): Observable<any> {
    return this.http.put<any>(`${this.backendUrl}/favalbums/${usersid}/${albumid}`, null, {
      headers: new HttpHeaders({
        'Authorization': this.token
      })
    });
  }

  saveFavBooks(usersid: number, bookid: string): Observable<any> {
    return this.http.put<any>(`${this.backendUrl}/favbooks/${usersid}/${bookid}`, null, {
      headers: new HttpHeaders({
        'Authorization': this.token
      })
    });
  }

  saveFavGames(usersid: number, gameid: string): Observable<any> {
    return this.http.put<any>(`${this.backendUrl}/favgames/${usersid}/${gameid}`, null, {
      headers: new HttpHeaders({
        'Authorization': this.token
      })
    });
  }

  deleteFavMovies(usersid: number, movieid: number): Observable<any> {
    return this.http.delete<any>(`${this.backendUrl}/favmovie/delete/${usersid}/${movieid}`, {
      headers: new HttpHeaders({
        'Authorization': this.token
      })
    });
  }

  deleteFavShows(usersid: number, showid: number): Observable<any> {
    return this.http.delete<any>(`${this.backendUrl}/favshows/delete/${usersid}/${showid}`, {
      headers: new HttpHeaders({
        'Authorization': this.token
      })
    });
  }

  deleteFavAlbums(usersid: number, albumid: string): Observable<any> {
    return this.http.delete<any>(`${this.backendUrl}/favalbums/delete/${usersid}/${albumid}`, {
      headers: new HttpHeaders({
        'Authorization': this.token
      })
    });
  }

  deleteFavBooks(usersid: number, bookid: string): Observable<any> {
    return this.http.delete<any>(`${this.backendUrl}/favbooks/delete/${usersid}/${bookid}`, {
      headers: new HttpHeaders({
        'Authorization': this.token
      })
    });
  }

  deleteFavGames(usersid: number, gameid: string): Observable<any> {
    return this.http.delete<any>(`${this.backendUrl}/favgames/delete/${usersid}/${gameid}`, {
      headers: new HttpHeaders({
        'Authorization': this.token
      })
    });
  }

  uploadImage(file: File, id: number): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<string>(`${this.backendUrl}/upload/${id}`, formData, {
      headers: new HttpHeaders({
        'Authorization': this.token
      })
    });
  }

  checkToken() {
    return this.http.post(this.backendUrl + '/check', {}, {
      headers: new HttpHeaders({
        'Authorization': this.token
      }),
      responseType: 'text'
    }).pipe(
      catchError(error => {
        return of(null);
      })
    ).subscribe((value) => {
      if (value != 'VALID') {
        localStorage.removeItem("forum_user_id");
        localStorage.removeItem("forum_access_token");
      }
    });
  }

  getImage(id: number): Observable<Blob> {
    return this.http.get(`${this.backendUrl}/images/${id}`, { responseType: 'blob' });
  }

}
