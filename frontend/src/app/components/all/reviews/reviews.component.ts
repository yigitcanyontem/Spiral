import { Component, Input, SimpleChanges } from '@angular/core';
import { PaginatedReviewDTO } from '../../../models/paginated-review-dto';
import { ReactionType } from '../../../enums/reaction-type';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { EntertainmentService } from '../../../services/entertainment.service';
import { ReviewService } from '../../../services/review.service';
import { EntertainmentType } from '../../../enums/entertainment-type';
import { ReviewCreateDTO } from '../../../models/review-create-dto';

@Component({
  selector: 'app-reviews',
  templateUrl: './reviews.component.html',
  styleUrls: ['./reviews.component.scss'],
})
export class ReviewsComponent {
  @Input()
  reviews: PaginatedReviewDTO | undefined;
  isLoaded = false;
  usersid = <string>localStorage.getItem('forum_user_id');
  id = <string>this.activatedRoute.snapshot.paramMap.get('id');
  entertainmentType = EntertainmentType.MOVIE;
  pageNumber: number = 0;
  pageSize: number = 10;
  sort: string = 'id';
  direction: string = 'ASC';
  rating = null;
  constructor(
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private entertainmentService: EntertainmentService,
    private reviewService: ReviewService,
  ) {}
  ngOnChanges(changes: SimpleChanges): void {
    if ('reviews' in changes) {
      this.isLoaded = true;
    }
  }

  upvote(id: number) {
    if (
      this.usersid == undefined ||
      isNaN(Number(this.usersid))
    ) {
      alert('Login');
    } else {
      this.reviewService
        .createReaction({
          reactionType: ReactionType.UPVOTE,
          reviewid: id,
          usersid: parseInt(this.usersid),
        })
        .subscribe((value) => {
          this.onGetReviews();
        });
    }
  }

  downvote(id: number) {
    if (
      this.usersid == undefined ||
      isNaN(Number(this.usersid))
    ) {
      alert('Login');
    } else {
      this.reviewService
        .createReaction({
          reactionType: ReactionType.DOWNVOTE,
          reviewid: id,
          usersid: parseInt(this.usersid),
        })
        .subscribe((value) => {
          this.onGetReviews();
        });
    }
  }

  onGetReviews() {
    this.reviewService
      .getReviewsByEntertainment(
        this.id,
        this.entertainmentType,
        this.pageNumber,
        this.pageSize,
        this.sort,
        this.direction,
        this.rating,
      )
      .subscribe((value) => {
        this.reviews = value;
      });
  }
}
