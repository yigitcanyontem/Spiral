import {Component, OnInit} from '@angular/core';
import {EntertainmentType} from "../../../enums/entertainment-type";
import {Movie} from "../../../models/movie";
import {PaginatedReviewDTO} from "../../../models/paginated-review-dto";
import {ActivatedRoute} from "@angular/router";
import {EntertainmentService} from "../../../services/entertainment.service";
import {ReviewService} from "../../../services/review.service";
import {forkJoin} from "rxjs";
import {Show} from "../../../models/show";

@Component({
  selector: 'app-show',
  templateUrl: './show.component.html',
  styleUrls: ['./show.component.scss']
})
export class ShowComponent implements OnInit{
  id = <string>this.activatedRoute.snapshot.paramMap.get('id');
  entertainmentType = EntertainmentType.SHOW;
  pageNumber: number = 0;
  pageSize: number = 10;
  sort: string = 'upvote';
  direction: string = 'ASC';
  rating = null;

  isLoaded = false;
  show!: Show;
  reviews!: PaginatedReviewDTO ;
  average!: number;

  constructor(private activatedRoute: ActivatedRoute,private entertainmentService:EntertainmentService, private reviewService:ReviewService) {
  }


  ngOnInit(): void {
    const getShow$ = this.entertainmentService.onGetShow((parseInt(this.id)));
    const getReviews$ = this.reviewService.getReviewsByEntertainment(this.id,this.entertainmentType,this.pageNumber,this.pageSize,this.sort,this.direction,this.rating);
    const getRating$ = this.reviewService.getAverageRatingByEntertainment(this.id,this.entertainmentType);
    forkJoin({
      show: getShow$,
      reviews: getReviews$,
      average: getRating$
    }).subscribe(
      (results: {
        show: Show,
        reviews: PaginatedReviewDTO,
        average: number
      }) => {
        this.show = results.show;
        this.reviews = results.reviews;
        this.average = results.average;
        console.log(this.show)
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
