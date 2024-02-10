import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { ReviewService } from '../../../services/review.service';
import { EntertainmentType } from '../../../enums/entertainment-type';
import { Review } from '../../../models/review';
import { ReactionType } from '../../../enums/reaction-type';
import { ConfirmationService, MessageService } from 'primeng/api';
import { PaginatorState } from 'primeng/paginator';
import { Subject, takeUntil } from 'rxjs';

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
  pageNumber!: number;
  pageSize: number = 10;
  sort: string = 'id';
  direction: string = 'ASC';
  rating = null;
  edit_frm: boolean = false;
  review_edit_id!: number;
  totalItems!: number;
  isLoaded = false;
  reviews!: Review[];
  own_reviews = this.userid === parseInt(this.usersid);

  constructor(
    private activatedRoute: ActivatedRoute,
    private userService: UserService,
    private router: Router,
    private reviewService: ReviewService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
  ) {}

  ngOnInit(): void {
    this.onGetReviewsByUser();
  }

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
            this.onGetReviewsByUser();
          },
          (error) => {
            this.ownCommentToast();
          },
        );
    }
  }

  ownCommentToast() {
    this.messageService.add({
      severity: 'error',
      summary: 'Error',
      detail: "Can't react to your own comment.",
    });
  }

  downvote(id: number) {
    if (this.usersid == undefined || isNaN(Number(this.usersid))) {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Login to downvote',
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
            this.onGetReviewsByUser();
          },
          (error) => {
            this.ownCommentToast();
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
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((value) => {
        this.reviews = value.reviews;
        this.totalItems = value.totalItems;
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

  onPageChange($event: PaginatorState) {
    if ($event.page != null) {
      this.pageNumber = $event.page;
    }

    if ($event.rows != null) {
      this.pageSize = $event.rows;
    }

    this.ngUnsubscribe.next();
    this.onGetReviewsByUser();
    window.scroll({
      top: 0,
      left: 0,
      behavior: 'smooth',
    });
  }

  public ngOnDestroy(): void {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }

  protected ngUnsubscribe: Subject<void> = new Subject<void>();
}
