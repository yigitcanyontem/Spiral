import {Component, OnInit} from '@angular/core';
import {EntertainmentType} from "../../../enums/entertainment-type";
import {Movie} from "../../../models/movie";
import {PaginatedReviewDTO} from "../../../models/paginated-review-dto";
import {ActivatedRoute} from "@angular/router";
import {EntertainmentService} from "../../../services/entertainment.service";
import {ReviewService} from "../../../services/review.service";
import {forkJoin} from "rxjs";
import {Game} from "../../../models/game";

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit{
  id = <string>this.activatedRoute.snapshot.paramMap.get('id');
  entertainmentType = EntertainmentType.GAME;
  pageNumber: number = 0;
  pageSize: number = 10;
  sort: string = 'upvote';
  direction: string = 'ASC';
  rating = null;

  isLoaded = false;
  game!: Game;
  reviews!: PaginatedReviewDTO ;
  average!: number;

  constructor(private activatedRoute: ActivatedRoute,private entertainmentService:EntertainmentService, private reviewService:ReviewService) {
  }


  ngOnInit(): void {
    const getGame$ = this.entertainmentService.onGetGame(this.id);
    const getReviews$ = this.reviewService.getReviewsByEntertainment(this.id,this.entertainmentType,this.pageNumber,this.pageSize,this.sort,this.direction,this.rating);
    const getRating$ = this.reviewService.getAverageRatingByEntertainment(this.id,this.entertainmentType);
    forkJoin({
      game: getGame$,
      reviews: getReviews$,
      average: getRating$
    }).subscribe(
      (results: {
        game: Game,
        reviews: PaginatedReviewDTO,
        average: number
      }) => {
        this.game = results.game;
        this.reviews = results.reviews;
        this.average = results.average;
        console.log(this.game)
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
