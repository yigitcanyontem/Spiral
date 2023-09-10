import { Component, OnInit } from '@angular/core';
import { Movie } from '../../../models/movie';
import { ActivatedRoute } from '@angular/router';
import { forkJoin } from 'rxjs';
import { EntertainmentService } from '../../../services/entertainment.service';
import { ReviewService } from '../../../services/review.service';
import { EntertainmentType } from '../../../enums/entertainment-type';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../../services/user.service';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-movie',
  templateUrl: './movie.component.html',
  styleUrls: ['./movie.component.scss'],
  providers: [MessageService],
})
export class MovieComponent implements OnInit {
  frm: FormGroup;
  userLogged!: boolean;
  favorited!: boolean;

  usersid = <string>localStorage.getItem('forum_user_id');
  id = <string>this.activatedRoute.snapshot.paramMap.get('id');
  entertainmentType = EntertainmentType.MOVIE;
  pageNumber: number = 0;
  pageSize: number = 10;
  sort: string = 'id';
  direction: string = 'ASC';
  rating = null;

  isLoaded = false;
  movie!: Movie;
  average!: number;

  error = false;

  constructor(
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private entertainmentService: EntertainmentService,
    private reviewService: ReviewService,
    private userService: UserService,
    private messageService: MessageService,
  ) {
    this.frm = this.formBuilder.group({
      usersid: [this.usersid],
      entertainmentType: [EntertainmentType.MOVIE],
      entertainmentid: [this.id],
      description: [''],
      title: ['', [Validators.required]],
      rating: ['', [Validators.required, Validators.min(1), Validators.max(5)]],
    });
  }

  onGetAverage() {
    this.reviewService
      .getAverageRatingByEntertainment(this.id, this.entertainmentType)
      .subscribe((results) => {
        this.average = results;
      });
  }

  ngOnInit(): void {
    const getMovie$ = this.entertainmentService.onGetMovie(parseInt(this.id));
    const getRating$ = this.reviewService.getAverageRatingByEntertainment(
      this.id,
      this.entertainmentType,
    );
    forkJoin({
      movie: getMovie$,
      average: getRating$,
    }).subscribe(
      (results: { movie: Movie; average: number }) => {
        this.movie = results.movie;
        this.average = results.average;
        this.checkUser();
        this.isLoaded = true;
      },
      (error) => {
        this.error = true;
        this.isLoaded = true;
      },
    );
  }

  checkUser() {
    this.userService
      .checkFavoritedMovie(parseInt(this.usersid), parseInt(this.id))
      .subscribe(
        (results) => {
          this.favorited = results;
        },
        (error) => {
          this.favorited = false;
        },
      );
    this.userLogged = this.usersid !== null;
  }

  addFavorite() {
    this.userService
      .saveFavMovies(parseInt(this.usersid), parseInt(this.id))
      .subscribe(
        (results) => {
          this.favorited = true;
        },
        (error) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'An Error Occured.',
          });
        },
      );
  }

  deleteFavorite() {
    this.userService
      .deleteFavMovies(parseInt(this.usersid), parseInt(this.id))
      .subscribe(
        (results) => {
          this.favorited = false;
        },
        (error) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'An Error Occured.',
          });
        },
      );
  }
}
