import {Component, OnInit} from '@angular/core';
import {EntertainmentType} from "../../../enums/entertainment-type";
import {PaginatedReviewDTO} from "../../../models/paginated-review-dto";
import {ActivatedRoute} from "@angular/router";
import {EntertainmentService} from "../../../services/entertainment.service";
import {ReviewService} from "../../../services/review.service";
import {forkJoin} from "rxjs";
import {Book} from "../../../models/book";

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.scss']
})
export class BookComponent implements OnInit{
  id = <string>this.activatedRoute.snapshot.paramMap.get('id');
  entertainmentType = EntertainmentType.BOOK;
  pageNumber: number = 0;
  pageSize: number = 10;
  sort: string = 'upvote';
  direction: string = 'ASC';
  rating = null;

  isLoaded = false;
  book!: Book;
  reviews!: PaginatedReviewDTO ;
  average!: number;

  constructor(private activatedRoute: ActivatedRoute,private entertainmentService:EntertainmentService, private reviewService:ReviewService) {
  }


  ngOnInit(): void {
    const getBook$ = this.entertainmentService.onGetBook(this.id);
    const getReviews$ = this.reviewService.getReviewsByEntertainment(this.id,this.entertainmentType,this.pageNumber,this.pageSize,this.sort,this.direction,this.rating);
    const getRating$ = this.reviewService.getAverageRatingByEntertainment(this.id,this.entertainmentType);
    forkJoin({
      book: getBook$,
      reviews: getReviews$,
      average: getRating$
    }).subscribe(
      (results: {
        book: Book,
        reviews: PaginatedReviewDTO,
        average: number
      }) => {
        this.book = results.book;
        this.reviews = results.reviews;
        this.average = results.average;
        console.log(this.book)
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
