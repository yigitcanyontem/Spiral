import {Component, OnInit} from '@angular/core';
import {Movie} from "../../../models/movie";
import {ActivatedRoute} from "@angular/router";
import {forkJoin} from "rxjs";
import {EntertainmentService} from "../../../services/entertainment.service";
import {ReviewService} from "../../../services/review.service";
import {PaginatedReviewDTO} from "../../../models/paginated-review-dto";
import {EntertainmentType} from "../../../enums/entertainment-type";

@Component({
  selector: 'app-movie',
  templateUrl: './movie.component.html',
  styleUrls: ['./movie.component.scss']
})
export class MovieComponent implements OnInit{
  id = <string>this.activatedRoute.snapshot.paramMap.get('id');
  entertainmentType = EntertainmentType.MOVIE;
  pageNumber: number = 0;
  pageSize: number = 10;
  sort: string = 'upvote';
  direction: string = 'ASC';
  rating = null;

  isLoaded = false;
  movie!: Movie ;
  reviews!: PaginatedReviewDTO ;
  average!: number;

  constructor(private activatedRoute: ActivatedRoute,private entertainmentService:EntertainmentService, private reviewService:ReviewService) {
  }


  ngOnInit(): void {
    const getMovie$ = this.entertainmentService.onGetMovie((parseInt(this.id)));
    const getReviews$ = this.reviewService.getReviewsByEntertainment(this.id,this.entertainmentType,this.pageNumber,this.pageSize,this.sort,this.direction,this.rating);
    const getRating$ = this.reviewService.getAverageRatingByEntertainment(this.id,this.entertainmentType);
    forkJoin({
      movie: getMovie$,
      reviews: getReviews$,
        average: getRating$
    }).subscribe(
      (results: {
        movie: Movie,
        reviews: PaginatedReviewDTO,
          average: number
      }) => {
        this.movie = results.movie;
        this.reviews = results.reviews;
        this.average = results.average;
        console.log(this.movie)
        console.log(this.reviews)
        console.log(this.average)
        this.isLoaded = true;
      },
      error => {
        // Handle errors
      }
    );

  }
}
