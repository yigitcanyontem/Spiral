import { Component, OnInit } from '@angular/core';
import { EntertainmentType } from '../../../enums/entertainment-type';
import { ActivatedRoute } from '@angular/router';
import { EntertainmentService } from '../../../services/entertainment.service';
import { ReviewService } from '../../../services/review.service';
import { forkJoin } from 'rxjs';
import { Game } from '../../../models/game';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss'],
})
export class GameComponent implements OnInit {
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

  constructor(
    private activatedRoute: ActivatedRoute,
    private entertainmentService: EntertainmentService,
    private reviewService: ReviewService,
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
