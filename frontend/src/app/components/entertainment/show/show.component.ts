import { Component, OnInit } from '@angular/core';
import { EntertainmentType } from '../../../enums/entertainment-type';
import { ActivatedRoute } from '@angular/router';
import { EntertainmentService } from '../../../services/entertainment.service';
import { ReviewService } from '../../../services/review.service';
import { forkJoin } from 'rxjs';
import { Show } from '../../../models/show';
import { UserService } from '../../../services/user.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-show',
  templateUrl: './show.component.html',
  styleUrls: ['./show.component.scss'],
  providers: [MessageService],
})
export class ShowComponent implements OnInit {
  id = <string>this.activatedRoute.snapshot.paramMap.get('id');
  usersid = <string>localStorage.getItem('forum_user_id');
  entertainmentType = EntertainmentType.SHOW;
  pageNumber: number = 0;
  pageSize: number = 10;
  sort: string = 'upvote';
  direction: string = 'ASC';
  rating = null;
  userLogged!: boolean;
  isLoaded = false;
  show!: Show;
  average!: number;

  favorited!: boolean;

  error = false;

  constructor(
    private activatedRoute: ActivatedRoute,
    private entertainmentService: EntertainmentService,
    private reviewService: ReviewService,
    private userService: UserService,
    private messageService: MessageService,
  ) {}

  ngOnInit(): void {
    const getShow$ = this.entertainmentService.onGetShow(parseInt(this.id));

    const getRating$ = this.reviewService.getAverageRatingByEntertainment(
      this.id,
      this.entertainmentType,
    );
    forkJoin({
      show: getShow$,
      average: getRating$,
    }).subscribe(
      (results: { show: Show; average: number }) => {
        this.show = results.show;
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
      .checkFavoritedShows(parseInt(this.usersid), parseInt(this.id))
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
      .saveFavShows(parseInt(this.usersid), parseInt(this.id))
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
      .deleteFavShows(parseInt(this.usersid), parseInt(this.id))
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
