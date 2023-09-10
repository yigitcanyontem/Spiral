import { Component, OnInit } from '@angular/core';
import { EntertainmentType } from '../../../enums/entertainment-type';
import { ActivatedRoute } from '@angular/router';
import { EntertainmentService } from '../../../services/entertainment.service';
import { ReviewService } from '../../../services/review.service';
import { forkJoin } from 'rxjs';
import { Game } from '../../../models/game';
import { MessageService } from 'primeng/api';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss'],
  providers: [MessageService],
})
export class GameComponent implements OnInit {
  userLogged!: boolean;
  favorited!: boolean;

  usersid = <string>localStorage.getItem('forum_user_id');
  id = <string>this.activatedRoute.snapshot.paramMap.get('id');
  entertainmentType = EntertainmentType.GAME;
  pageNumber: number = 0;
  pageSize: number = 10;
  sort: string = 'upvote';
  direction: string = 'ASC';
  rating = null;
  responsiveOptions: any[] | undefined;
  isLoaded = false;
  game!: Game;
  average!: number;

  error = false;

  constructor(
    private activatedRoute: ActivatedRoute,
    private entertainmentService: EntertainmentService,
    private reviewService: ReviewService,
    private userService: UserService,
    private messageService: MessageService,
  ) {
    this.responsiveOptions = [
      {
        breakpoint: '1024px',
        numVisible: 5,
      },
      {
        breakpoint: '768px',
        numVisible: 3,
      },
      {
        breakpoint: '560px',
        numVisible: 1,
      },
    ];
  }

  ngOnInit(): void {
    const getGame$ = this.entertainmentService.onGetGame(this.id);
    const getRating$ = this.reviewService.getAverageRatingByEntertainment(
      this.id,
      this.entertainmentType,
    );
    forkJoin({
      game: getGame$,
      average: getRating$,
    }).subscribe(
      (results: { game: Game; average: number }) => {
        this.game = results.game;
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
      .checkFavoritedGames(parseInt(this.usersid), this.id)
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
      .saveFavGames({
        usersid: parseInt(this.usersid),
        gameid: this.id,
        gameName: this.game.name,
        gameImage: this.game.original_url,
      })
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
    this.userService.deleteFavGames(parseInt(this.usersid), this.id).subscribe(
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
