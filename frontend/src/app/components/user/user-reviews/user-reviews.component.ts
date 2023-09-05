import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../../services/user.service";
import {EntertainmentService} from "../../../services/entertainment.service";
import {ReviewService} from "../../../services/review.service";
import {EntertainmentType} from "../../../enums/entertainment-type";
import {Show} from "../../../models/show";
import {PaginatedReviewDTO} from "../../../models/paginated-review-dto";
import {forkJoin} from "rxjs";
import {Review} from "../../../models/review";
import {EntertainmentAllDTO} from "../../../models/entertainment-all-dto";

@Component({
  selector: 'app-user-reviews',
  templateUrl: './user-reviews.component.html',
  styleUrls: ['./user-reviews.component.scss']
})
export class UserReviewsComponent implements OnInit {
  userid = parseInt(<string>this.activatedRoute.snapshot.paramMap.get('userid'));
  own_reviews = this.userid === parseInt(<string>localStorage.getItem("forum_user_id"));

  constructor(private activatedRoute: ActivatedRoute, private userService: UserService, private router: Router, private reviewService: ReviewService) {
  }

  dtos: EntertainmentAllDTO[] = [];
  pageNumber: number = 0;
  pageSize: number = 10;
  sort: string = 'upvote';
  direction: string = 'ASC';
  rating = null;

  totalpages!: number;
  isLoaded = false;
  reviews!: Review[];

  ngOnInit(): void {
    this.reviewService.getReviewsByUser(
      this.userid,
      this.pageNumber,
      this.pageSize,
      this.sort,
      this.direction,
      this.rating
    ).subscribe((value) => {
        this.reviews = value.reviews;
        this.totalpages = value.totalPages;
        this.isLoaded = true;
      }
    )
  }

  protected readonly EntertainmentType = EntertainmentType;
}
