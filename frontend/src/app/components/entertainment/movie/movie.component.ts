import { Component, OnInit } from '@angular/core';
import { Movie } from '../../../models/movie';
import { ActivatedRoute } from '@angular/router';
import { forkJoin } from 'rxjs';
import { EntertainmentService } from '../../../services/entertainment.service';
import { ReviewService } from '../../../services/review.service';
import { PaginatedReviewDTO } from '../../../models/paginated-review-dto';
import { EntertainmentType } from '../../../enums/entertainment-type';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RegisterRequest } from '../../../models/register-request';
import { ReviewCreateDTO } from '../../../models/review-create-dto';
import { ReactionCreateDto } from '../../../models/reaction-create-dto';
import { ReactionType } from '../../../enums/reaction-type';

@Component({
  selector: 'app-movie',
  templateUrl: './movie.component.html',
  styleUrls: ['./movie.component.scss'],
})
export class MovieComponent implements OnInit {
  frm: FormGroup;

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
  reviews!: PaginatedReviewDTO;
  average!: number;

  constructor(
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private entertainmentService: EntertainmentService,
    private reviewService: ReviewService,
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
  submitForm() {
    this.reviewService
      .saveReview({
        usersid: parseInt(this.usersid),
        entertainmentType: EntertainmentType.MOVIE,
        entertainmentid: this.id,
        description: this.description?.value,
        title: this.title?.value,
        rating: this.review_rating?.value,
      } as ReviewCreateDTO)
      .subscribe((bool) => {
        if (bool) {
          this.onGetReviews();
        }
      });
  }

  onGetReviews() {
    this.reviewService
      .getReviewsByEntertainment(
        this.id,
        this.entertainmentType,
        this.pageNumber,
        this.pageSize,
        this.sort,
        this.direction,
        this.rating,
      )
      .subscribe((value) => {
        this.reviews = value;
      });
  }

  ngOnInit(): void {
    const getMovie$ = this.entertainmentService.onGetMovie(parseInt(this.id));
    const getReviews$ = this.reviewService.getReviewsByEntertainment(
      this.id,
      this.entertainmentType,
      this.pageNumber,
      this.pageSize,
      this.sort,
      this.direction,
      this.rating,
    );
    const getRating$ = this.reviewService.getAverageRatingByEntertainment(
      this.id,
      this.entertainmentType,
    );
    forkJoin({
      movie: getMovie$,
      reviews: getReviews$,
      average: getRating$,
    }).subscribe(
      (results: {
        movie: Movie;
        reviews: PaginatedReviewDTO;
        average: number;
      }) => {
        this.movie = results.movie;
        this.reviews = results.reviews;
        this.average = results.average;
        this.isLoaded = true;
      },
      (error) => {
        // Handle errors
      },
    );
  }

  upvote(id: number) {
    if (
      this.usersid == undefined ||
      this.usersid == null ||
      isNaN(Number(this.usersid))
    ) {
      alert('Login');
    } else {
      this.reviewService
        .createReaction({
          reactionType: ReactionType.UPVOTE,
          reviewid: id,
          usersid: parseInt(this.usersid),
        })
        .subscribe((value) => {
          this.onGetReviews();
        });
    }
  }
  downvote(id: number) {
    if (
      this.usersid == undefined ||
      this.usersid == null ||
      isNaN(Number(this.usersid))
    ) {
      alert('Login');
    } else {
      this.reviewService
        .createReaction({
          reactionType: ReactionType.DOWNVOTE,
          reviewid: id,
          usersid: parseInt(this.usersid),
        })
        .subscribe((value) => {
          this.onGetReviews();
        });
    }
  }
  get description() {
    return this.frm.get('description');
  }
  get title() {
    return this.frm.get('title');
  }
  get review_rating() {
    return this.frm.get('rating');
  }

  protected readonly console = console;
}
