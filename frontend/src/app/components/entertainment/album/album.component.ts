import {Component, OnInit} from '@angular/core';
import {EntertainmentType} from "../../../enums/entertainment-type";
import {Movie} from "../../../models/movie";
import {PaginatedReviewDTO} from "../../../models/paginated-review-dto";
import {ActivatedRoute} from "@angular/router";
import {EntertainmentService} from "../../../services/entertainment.service";
import {ReviewService} from "../../../services/review.service";
import {forkJoin} from "rxjs";
import {Album} from "../../../models/album";

@Component({
  selector: 'app-album',
  templateUrl: './album.component.html',
  styleUrls: ['./album.component.scss']
})
export class AlbumComponent implements OnInit{
  id = <string>this.activatedRoute.snapshot.paramMap.get('id');
  entertainmentType = EntertainmentType.ALBUM;
  pageNumber: number = 0;
  pageSize: number = 10;
  sort: string = 'upvote';
  direction: string = 'ASC';
  rating = null;

  isLoaded = false;
  album!: Album;
  reviews!: PaginatedReviewDTO ;
  average!: number;

  constructor(private activatedRoute: ActivatedRoute,private entertainmentService:EntertainmentService, private reviewService:ReviewService) {
  }


  ngOnInit(): void {
    const getAlbum$ = this.entertainmentService.onGetAlbum((this.id));
    const getReviews$ = this.reviewService.getReviewsByEntertainment(this.id,this.entertainmentType,this.pageNumber,this.pageSize,this.sort,this.direction,this.rating);
    const getRating$ = this.reviewService.getAverageRatingByEntertainment(this.id,this.entertainmentType);
    forkJoin({
      album: getAlbum$,
      reviews: getReviews$,
      average: getRating$
    }).subscribe(
      (results: {
        album: Album,
        reviews: PaginatedReviewDTO,
        average: number
      }) => {
        this.album = results.album;
        this.reviews = results.reviews;
        this.average = results.average;
        console.log(this.album)
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
