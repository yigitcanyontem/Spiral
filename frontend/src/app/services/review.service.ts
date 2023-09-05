import { Injectable } from '@angular/core';
import {environment} from "../environment/environment";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {PaginatedReviewDTO} from "../models/paginated-review-dto";
import {Review} from "../models/review";
import {ReviewUpdateDTO} from "../models/review-update-dto";
import {ReviewCreateDTO} from "../models/review-create-dto";
import {ReactionCreateDto} from "../models/reaction-create-dto";

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  private apiUrl = environment.apiUrl;
  private backendUrl = `${this.apiUrl}/review`;

  constructor(private http: HttpClient) { }

  token = `Bearer ${localStorage.getItem('forum_access_token')}`;

  getReviewById(id: number): Observable<Review> {
    return this.http.get<Review>(`${this.backendUrl}/${id}`);
  }

  getReviewsByUser(
    usersid: number,
    pageNumber: number = 0,
    pageSize: number = 100,
    sort: string = 'upvote',
    direction: string = 'ASC',
    rating: number | null = null
  ): Observable<PaginatedReviewDTO> {
    const params = new HttpParams()
      .set('usersid', usersid)
      .set('pageNumber', pageNumber.toString())
      .set('pageSize', pageSize.toString())
      .set('sort', sort)
      .set('direction', direction)
      .set('rating', rating?.toString() || '');

    return this.http.get<PaginatedReviewDTO>(`${this.backendUrl}/user`, { params });
  }

  getReviewsByEntertainment(
    entertainmentId: string,
    entertainmentType: string,
    pageNumber: number = 0,
    pageSize: number = 100,
    sort: string = 'id',
    direction: string = 'ASC',
    rating: number | null = null
  ): Observable<PaginatedReviewDTO> {
    const params = new HttpParams()
      .set('entertainmentId', entertainmentId)
      .set('entertainmentType', entertainmentType)
      .set('pageNumber', pageNumber.toString())
      .set('pageSize', pageSize.toString())
      .set('sort', sort)
      .set('direction', direction)
      .set('rating', rating?.toString() || '');

    return this.http.get<PaginatedReviewDTO>(`${this.backendUrl}/entertainment`, { params });
  }

  getAverageRatingByEntertainment(entertainmentId: string,entertainmentType: string): Observable<number> {
    const params = new HttpParams()
      .set('entertainmentId', entertainmentId)
      .set('entertainmentType', entertainmentType);


    return this.http.get<number>(`${this.backendUrl}/average-rating`, { params });
  }

  updateReview(usersid: number, reviewUpdateDTO: ReviewUpdateDTO): Observable<Review> {
    return this.http.patch<Review>(`${this.backendUrl}/update/${usersid}`, reviewUpdateDTO, {
      headers: new HttpHeaders({
        'Authorization': this.token
      })
    });
  }

  createReaction(reactionCreateDto: ReactionCreateDto): Observable<Review> {
    return this.http.post<Review>(`${this.backendUrl}/reaction`, reactionCreateDto, {
      headers: new HttpHeaders({
        'Authorization': this.token
      })
    });
  }

  deleteReview(usersid: number, id: number): Observable<any> {
    return this.http.delete<any>(`${this.backendUrl}/delete/${usersid}/${id}`, {
      headers: new HttpHeaders({
        'Authorization': this.token
      })
    });
  }

  saveReview(reviewCreateDTO: ReviewCreateDTO): Observable<Review> {
    return this.http.post<Review>(`${this.backendUrl}/save`, reviewCreateDTO, {
      headers: new HttpHeaders({
        'Authorization': this.token
      })
    });
  }
}
