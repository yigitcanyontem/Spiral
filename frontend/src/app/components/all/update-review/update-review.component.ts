import {
  Component,
  EventEmitter,
  Input,
  Output,
  SimpleChanges,
} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReviewService } from '../../../services/review.service';
import { ReviewUpdateDTO } from '../../../models/review-update-dto';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-update-review',
  templateUrl: './update-review.component.html',
  styleUrls: ['./update-review.component.scss'],
  providers: [ConfirmationService, MessageService],
})
export class UpdateReviewComponent {
  frm!: FormGroup;
  @Output()
  getReviews = new EventEmitter<string>();

  @Input()
  review_edit_id!: number;

  isLoaded = false;
  usersid = <string>localStorage.getItem('forum_user_id');

  ngOnChanges(changes: SimpleChanges): void {
    if ('review_edit_id' in changes) {
      this.reviewService
        .getReviewById(this.review_edit_id)
        .subscribe((review) => {
          this.frm = this.formBuilder.group({
            id: [this.review_edit_id],
            usersid: [this.usersid],
            title: [review.title],
            description: [review.description],
            rating: [
              review.rating,
              [Validators.required, Validators.min(1), Validators.max(5)],
            ],
          });
          this.isLoaded = true;
        });
    }
  }

  constructor(
    private formBuilder: FormBuilder,
    private reviewService: ReviewService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
  ) {}

  submitForm() {
    this.reviewService
      .updateReview({
        id: this.review_edit_id,
        usersid: parseInt(this.usersid),
        description: this.description?.value,
        title: this.title?.value,
        rating: this.review_rating?.value,
      } as ReviewUpdateDTO)
      .subscribe(
        (value) => {
          this.messageService.add({
            severity: 'info',
            summary: 'Confirmed',
            detail: 'Review has been updated.',
          });
          this.getReviews.next('');
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

  confirmEdit(event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure that you want to save the changes?',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.submitForm();
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
}
