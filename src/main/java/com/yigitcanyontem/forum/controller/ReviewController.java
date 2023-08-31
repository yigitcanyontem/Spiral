package com.yigitcanyontem.forum.controller;

import com.yigitcanyontem.forum.entity.Review;
import com.yigitcanyontem.forum.entity.enums.EntertainmentType;
import com.yigitcanyontem.forum.model.review.PaginatedReviewDTO;
import com.yigitcanyontem.forum.model.review.ReviewCreateDTO;
import com.yigitcanyontem.forum.model.review.ReviewDTO;
import com.yigitcanyontem.forum.model.review.ReviewUpdateDTO;
import com.yigitcanyontem.forum.service.ReviewService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{id}")
    @Cacheable(value = "review", key = "'id-' + #id")
    public ResponseEntity<ReviewDTO> getReviewsById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(reviewService.getReviewsDTOById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user")
    @Cacheable(value = "review_user", key = "'usersid-' + #usersid + '-' + #pageNumber + '-' + #pageSize + '-' + #sort + '-' + #direction")
    public ResponseEntity<PaginatedReviewDTO> getReviewsByUser(
            @RequestParam Integer usersid,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "100") int pageSize,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(required = false) @Min(1) @Max(5) Integer rating
    ) {
        try {
            return ResponseEntity.ok(reviewService.getReviewsByUser(usersid, pageNumber, pageSize, sort, direction,rating));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/entertainment")
    public ResponseEntity<PaginatedReviewDTO> getReviewsByEntertainment(
            @RequestParam EntertainmentType entertainmentType,
            @RequestParam String entertainmentId,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "100") int pageSize,
            @RequestParam(value = "sort", defaultValue = "upvote") String sort,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(required = false) @Min(1) @Max(5) Integer rating

    ) {
        try {
            return ResponseEntity.ok(reviewService.getReviewsByEntertainment(entertainmentType, entertainmentId, pageNumber, pageSize, sort, direction,rating));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/average-rating")
    public ResponseEntity<Double> getAverageRatingByEntertainment(
            @RequestParam EntertainmentType entertainmentType,
            @RequestParam String entertainmentId
    ) {
        try {
            return ResponseEntity.ok(reviewService.getAverageRatingByEntertainment(entertainmentType, entertainmentId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/update/{usersid}")
    @Caching(evict = {
            @CacheEvict(value = "review", key = "'id-' + #reviewUpdateDTO.id"),
            @CacheEvict(value = "review_user", allEntries = true)
    }
    )
    public ResponseEntity<Review> updateReview(@PathVariable Integer usersid,@RequestBody ReviewUpdateDTO reviewUpdateDTO)  {
        try {
            return ResponseEntity.ok(reviewService.updateReview(reviewUpdateDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/upvote/{usersid}/{id}")
    @Caching(evict = {
            @CacheEvict(value = "review", key = "'id-' + #id"),
            @CacheEvict(value = "review_user", allEntries = true)
    })
    public ResponseEntity<Integer> incrementUpvote(@PathVariable Integer usersid,@PathVariable Long id) {
        try {
            return ResponseEntity.ok(reviewService.incrementUpvote(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/downvote/{usersid}/{id}")
    @Caching(evict = {
            @CacheEvict(value = "review", key = "'id-' + #id"),
            @CacheEvict(value = "review_user", allEntries = true)
    })
    public ResponseEntity<Integer> incrementDownvote(@PathVariable Integer usersid,@PathVariable Long id) {
        try {
            return ResponseEntity.ok(reviewService.incrementDownvote(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{usersid}/{id}")
    @Caching(evict = {
            @CacheEvict(value = "review", key = "'id-' + #id"),
            @CacheEvict(value = "review_user", allEntries = true)
    })
    public ResponseEntity<?> deleteReview(@PathVariable Integer usersid,@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/save")
    @Caching(evict = {
            @CacheEvict(value = "review_user", key = "'usersid-' + #reviewCreateDTO.usersid"),
            @CacheEvict(value = "review_user", allEntries = true)
    })
    public ResponseEntity<Review> saveReview(@RequestBody ReviewCreateDTO reviewCreateDTO) {
        try {
            return ResponseEntity.ok(reviewService.saveReview(reviewCreateDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
