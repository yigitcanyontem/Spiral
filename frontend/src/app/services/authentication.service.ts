import { Injectable } from '@angular/core';
import {catchError, map, Observable, of} from "rxjs";
import {environment} from "../environment/environment";
import {AuthenticationResponse} from "../models/authentication-response";
import {AuthenticationRequest} from "../models/authentication-request";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {RegisterRequest} from "../models/register-request";
import {Country} from "../models/country";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private apiUrl = environment.apiUrl;
  private backendUrl = `${this.apiUrl}/auth`;
  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
  private httpOptionsWithBearer = {
    headers: new HttpHeaders(
      { 'Content-Type': 'application/json' ,
        'Authorization': `Bearer ${localStorage.getItem('forum_access_token')}`}
    )
  };
  constructor(private http: HttpClient) { }

  onLogin(request: AuthenticationRequest): Observable<boolean> {
    return this.http.post<AuthenticationResponse>(this.backendUrl+"/authenticate", request, this.httpOptions).pipe(
      map((result: AuthenticationResponse) => {
        localStorage.setItem('forum_user_id', String(result.id));
        localStorage.setItem('forum_access_token', result.access_token);
        return true;
      }),
      catchError((error) => {
        console.error('Error:', error);
        return of(false);
      })
    );
  }

  onLogout()  {
    localStorage.removeItem("forum_user_id")
    localStorage.removeItem("forum_access_token")
    return this.http.post(this.backendUrl+'/logout', {});
  }

  onRegister(request: RegisterRequest): Observable<boolean> {
    return this.http.post<AuthenticationResponse>(this.backendUrl+"/register", request, this.httpOptions).pipe(
      map((result: AuthenticationResponse) => {
        localStorage.setItem('forum_user_id', String(result.id));
        localStorage.setItem('forum_access_token', result.access_token);
        return true;
      }),
      catchError((error) => {
        console.error('Error:', error);
        return of(false);
      })
    );
  }

  onGetCountries(): Observable<Country[]>{
    return this.http.get<Country[]>(this.backendUrl+`/countries`, this.httpOptionsWithBearer);
  }

}
