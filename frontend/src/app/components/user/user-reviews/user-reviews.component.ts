import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { ReviewService } from '../../../services/review.service';
import { EntertainmentType } from '../../../enums/entertainment-type';
import { Review } from '../../../models/review';
import { ReactionType } from '../../../enums/reaction-type';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-user-reviews',
  templateUrl: './user-reviews.component.html',
  styleUrls: ['./user-reviews.component.scss'],
  providers: [ConfirmationService, MessageService],
})
export class UserReviewsComponent implements OnInit {
  userid = parseInt(
    <string>this.activatedRoute.snapshot.paramMap.get('userid'),
  );
  usersid = <string>localStorage.getItem('forum_user_id');

  own_reviews = this.userid === parseInt(this.usersid);

  constructor(
    private activatedRoute: ActivatedRoute,
    private userService: UserService,
    private router: Router,
    private reviewService: ReviewService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
  ) {}

  pageNumber: number = 0;
  pageSize: number = 10;
  sort: string = 'id';
  direction: string = 'ASC';
  rating = null;
  edit_frm: boolean = false;
  review_edit_id!: number;
  totalpages!: number;
  isLoaded = false;
  reviews!: Review[];

  upvote(id: number) {
    if (this.usersid == undefined || isNaN(Number(this.usersid))) {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Login to upvote',
      });
    } else {
      this.reviewService
        .createReaction({
          reactionType: ReactionType.UPVOTE,
          reviewid: id,
          usersid: parseInt(this.usersid),
        })
        .subscribe(
          (value) => {
            this.messageService.add({
              severity: 'success',
              summary: 'Upvoted',
              detail: 'Review has been upvoted',
            });
            this.onGetReviewsByUser();
          },
          (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: "Can't react to your own comment.",
            });
          },
        );
    }
  }

  downvote(id: number) {
    if (this.usersid == undefined || isNaN(Number(this.usersid))) {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Login to upvote',
      });
    } else {
      this.reviewService
        .createReaction({
          reactionType: ReactionType.DOWNVOTE,
          reviewid: id,
          usersid: parseInt(this.usersid),
        })
        .subscribe(
          (value) => {
            this.messageService.add({
              severity: 'success',
              summary: 'Downvoted',
              detail: 'Review has been downvoted',
            });
            this.onGetReviewsByUser();
          },
          (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: "Can't react to your own comment.",
            });
          },
        );
    }
  }
  deleteReview(id: number) {
    this.reviewService
      .deleteReview(parseInt(this.usersid), id)
      .subscribe((value) => {
        this.messageService.add({
          severity: 'info',
          summary: 'Confirmed',
          detail: 'Review has been deleted',
        });
        this.onGetReviewsByUser();
      });
  }
  onGetReviewsByUser() {
    this.reviewService
      .getReviewsByUser(
        this.userid,
        this.pageNumber,
        this.pageSize,
        this.sort,
        this.direction,
        this.rating,
      )
      .subscribe((value) => {
        this.reviews = value.reviews;
        this.totalpages = value.totalPages;
        if (this.review_edit_id != undefined) {
          this.edit_frm = false;
        }
        this.isLoaded = true;
      });
  }

  editReview(id: number) {
    this.review_edit_id = id;
    this.edit_frm = !this.edit_frm;
  }

  ngOnInit(): void {
    this.onGetReviewsByUser();
  }
  confirmDelete(event: Event, id: number) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure that you want to delete this review?',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.deleteReview(id);
      },
      reject: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Rejected',
          detail: 'Cancelled',
        });
      },
    });
  }
  protected readonly EntertainmentType = EntertainmentType;
  protected readonly parseInt = parseInt;
}
