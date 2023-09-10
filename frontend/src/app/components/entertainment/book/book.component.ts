import { Component, OnInit } from '@angular/core';
import { EntertainmentType } from '../../../enums/entertainment-type';
import { ActivatedRoute } from '@angular/router';
import { EntertainmentService } from '../../../services/entertainment.service';
import { ReviewService } from '../../../services/review.service';
import { forkJoin } from 'rxjs';
import { Book } from '../../../models/book';
import { MessageService } from 'primeng/api';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.scss'],
  providers: [MessageService],
})
export class BookComponent implements OnInit {
  userLogged!: boolean;
  favorited!: boolean;

  usersid = <string>localStorage.getItem('forum_user_id');
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

  error = false;

  constructor(
    private activatedRoute: ActivatedRoute,
    private entertainmentService: EntertainmentService,
    private reviewService: ReviewService,
    private userService: UserService,
    private messageService: MessageService,
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
        this.checkUser();
        this.isLoaded = true;
      },
      (error) => {
        this.error = true;
        this.isLoaded = true;
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

  checkUser() {
    this.userService
      .checkFavoritedBooks(parseInt(this.usersid), this.id)
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
    this.userService.saveFavBooks(parseInt(this.usersid), this.id).subscribe(
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
    this.userService.deleteFavBooks(parseInt(this.usersid), this.id).subscribe(
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
