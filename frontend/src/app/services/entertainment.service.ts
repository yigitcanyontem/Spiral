import { Injectable } from '@angular/core';
import {environment} from "../environment/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {CrewMember} from "../models/crew-member";
import {Book} from "../models/book";
import {Album} from "../models/album";
import {Movie} from "../models/movie";
import {Show} from "../models/show";
import {Game} from "../models/game";

@Injectable({
  providedIn: 'root'
})
export class EntertainmentService {

  private apiUrl = environment.apiUrl;

  private backendUrl = `${this.apiUrl}/entertainment`;

  constructor(private http: HttpClient) {
  }

  onGetShow(id:number): Observable<Show>{
    return this.http.get<Show>(this.backendUrl+`/show/${id}`);
  }

  onGetMovie(id:number): Observable<Movie>{
    return this.http.get<Movie>(this.backendUrl+`/movie/${id}`);
  }

  onGetGame(id:string): Observable<Game>{
    return this.http.get<Game>(this.backendUrl+`/game/${id}`);
  }

  onGetAlbum(id:string): Observable<Album>{
    return this.http.get<Album>(this.backendUrl+`/album/${id}`);
  }

  onGetBook(id:string): Observable<Book>{
    return this.http.get<Book>(this.backendUrl+`/book/${id}`);
  }

  onGetCrew(id:number): Observable<CrewMember>{
    return this.http.get<CrewMember>(this.backendUrl+`/crew/${id}`);
  }
}
