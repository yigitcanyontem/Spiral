import { Component, OnInit } from '@angular/core';
import { EntertainmentType } from '../../../enums/entertainment-type';
import { ActivatedRoute } from '@angular/router';
import { EntertainmentService } from '../../../services/entertainment.service';
import { ReviewService } from '../../../services/review.service';
import { forkJoin } from 'rxjs';
import { Book } from '../../../models/book';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.scss'],
})
export class BookComponent implements OnInit {
  id = <string>this.activatedRoute.snapshot.paramMap.get('id');
  entertainmentType = EntertainmentType.BOOK;
  pageNumber: number = 0;
  pageSize: number = 10;
  sort: string = 'upvote';
  direction: string = 'ASC';
  rating = null;

  isLoaded = false;
  book!: Book;
  average!: number;

  constructor(
    private activatedRoute: ActivatedRoute,
    private entertainmentService: EntertainmentService,
    private reviewService: ReviewService,
  ) {}

  ngOnInit(): void {
    const getBook$ = this.entertainmentService.onGetBook(this.id);
    const getRating$ = this.reviewService.getAverageRatingByEntertainment(
      this.id,
      this.entertainmentType,
    );
    forkJoin({
      book: getBook$,
      average: getRating$,
    }).subscribe(
      (results: { book: Book; average: number }) => {
        this.book = results.book;
        this.average = results.average;
        this.isLoaded = true;
      },
      (error) => {
        // Handle errors
      },
    );
  }

  onGetAverage() {
    this.reviewService
      .getAverageRatingByEntertainment(this.id, this.entertainmentType)
      .subscribe((results) => {
        this.average = results;
      });
  }
}
