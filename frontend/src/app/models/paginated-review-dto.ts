import {Review} from "./review";

export interface PaginatedReviewDTO {
  reviews: Review[];
  totalPages: number;
}

