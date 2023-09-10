import {
  Component,
  EventEmitter,
  Input,
  Output,
  SimpleChanges,
} from '@angular/core';
import { PaginatedReviewDTO } from '../../../models/paginated-review-dto';
import { ReactionType } from '../../../enums/reaction-type';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ReviewService } from '../../../services/review.service';
import { EntertainmentType } from '../../../enums/entertainment-type';
import { ReviewCreateDTO } from '../../../models/review-create-dto';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-reviews',
  templateUrl: './reviews.component.html',
  styleUrls: ['./reviews.component.scss'],
  providers: [ConfirmationService, MessageService],
})
export class ReviewsComponent {
  reviews: PaginatedReviewDTO = {} as PaginatedReviewDTO;
  @Input()
  entertainmentType!: EntertainmentType;
  @Input()
  entertainmentTitle!: string;
  @Input()
  entertainmentImage!: string;

  @Output()
  getAverage = new EventEmitter<string>();

  frm: FormGroup;
  isLoaded = false;
  usersid = <string>localStorage.getItem('forum_user_id');
  id = <string>this.activatedRoute.snapshot.paramMap.get('id');
  pageNumber: number = 0;
  pageSize: number = 10;
  sort: string = 'id';
  direction: string = 'ASC';
  rating = null;

  edit_frm: boolean = false;
  review_edit_id!: number;
  constructor(
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private reviewService: ReviewService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
  ) {
    this.frm = this.formBuilder.group({
      usersid: [this.usersid],
      entertainmentType: [this.entertainmentType],
      entertainmentid: [this.id],
      description: [''],
      title: [''],
      rating: ['', [Validators.required, Validators.min(1), Validators.max(5)]],
      entertainmentTitle: [this.entertainmentTitle],
      entertainmentImage: [this.entertainmentImage],
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if ('entertainmentType' in changes) {
      this.onGetReviews();
    }
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
            this.onGetReviews();
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
            this.onGetReviews();
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
        if (this.review_edit_id != undefined) {
          this.edit_frm = false;
        }
        this.getAverage.next('');
        this.isLoaded = true;
      });
  }

  submitForm() {
    this.reviewService
      .saveReview({
        usersid: parseInt(this.usersid),
        entertainmentType: this.entertainmentType,
        title: this.title?.value,
        entertainmentid: this.id,
        description: this.description?.value,
        rating: this.review_rating?.value,
        entertainmentTitle: this.entertainmentTitle,
        entertainmentImage: this.entertainmentImage,
      } as ReviewCreateDTO)
      .subscribe(
        (bool) => {
          if (bool) {
            this.messageService.add({
              severity: 'info',
              summary: 'Confirmed',
              detail: 'Review has been submitted.',
            });
            this.onGetReviews();
          }
        },
        (error) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'There was an error.',
          });
        },
      );
  }

  deleteReview(id: number) {
    this.reviewService.deleteReview(parseInt(this.usersid), id).subscribe(
      (value) => {
        this.messageService.add({
          severity: 'info',
          summary: 'Confirmed',
          detail: 'Review has been deleted',
        });
        this.onGetReviews();
      },
      (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'There was an error.',
        });
      },
    );
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
  get description() {
    return this.frm.get('description');
  }
  get title() {
    return this.frm.get('title');
  }
  get review_rating() {
    return this.frm.get('rating');
  }

  protected readonly parseInt = parseInt;
}
