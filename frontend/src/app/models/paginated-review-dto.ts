import { Review } from './review';

export interface PaginatedReviewDTO {
  reviews: Review[];
  totalItems: number;
}
