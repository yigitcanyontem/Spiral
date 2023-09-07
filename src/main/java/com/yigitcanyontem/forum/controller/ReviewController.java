package com.yigitcanyontem.forum.controller;

import com.yigitcanyontem.forum.entity.Review;
import com.yigitcanyontem.forum.entity.Token;
import com.yigitcanyontem.forum.entity.enums.EntertainmentType;
import com.yigitcanyontem.forum.entity.enums.Role;
import com.yigitcanyontem.forum.model.review.*;
import com.yigitcanyontem.forum.repository.TokenRepository;
import com.yigitcanyontem.forum.service.ReviewReactionService;
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
    private final TokenRepository tokenRepository;
    private final ReviewReactionService reviewReactionService;

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
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(required = false) @Min(1) @Max(5) Integer rating
    ) {
        return ResponseEntity.ok(reviewService.getReviewsByUser(usersid, pageNumber, pageSize, sort, direction, rating));

    }

    @GetMapping("/entertainment")
    public ResponseEntity<PaginatedReview> getReviewsByEntertainment(
            @RequestParam EntertainmentType entertainmentType,
            @RequestParam String entertainmentId,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(required = false) @Min(1) @Max(5) Integer rating
    ) {
        try {
            return ResponseEntity.ok(reviewService.getReviewsByEntertainment(entertainmentType, entertainmentId, pageNumber, pageSize, sort, direction, rating));
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
    @PatchMapping("/update")
    @Caching(evict = {
            @CacheEvict(value = "review", key = "'id-' + #reviewUpdateDTO.id"),
            @CacheEvict(value = "review_user", allEntries = true)
    }
    )
    public ResponseEntity<Review> updateReview(@RequestBody ReviewUpdateDTO reviewUpdateDTO, @RequestHeader(name = "Authorization") String token) {
        if (!userValid(reviewUpdateDTO.getUsersid(), token)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        try {
            return ResponseEntity.ok(reviewService.updateReview(reviewUpdateDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/reaction")
    @Caching(evict = {
            @CacheEvict(value = "review", key = "'id-' + #id"),
            @CacheEvict(value = "review_user", allEntries = true)
    })
    public ResponseEntity<Review> createReaction(@RequestBody ReactionCreateDTO reactionCreateDTO, @RequestHeader(name = "Authorization") String token) {
        if (!userValid(reactionCreateDTO.getUsersid(), token)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(reviewReactionService.addReaction(reactionCreateDTO));
    }


    @DeleteMapping("/delete/{usersid}/{id}")
    @Caching(evict = {
            @CacheEvict(value = "review", key = "'id-' + #id"),
            @CacheEvict(value = "review_user", allEntries = true)
    })
    public ResponseEntity<?> deleteReview(@PathVariable Integer usersid, @PathVariable Long id, @RequestHeader(name = "Authorization") String token) {
        if (!userValid(usersid, token)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
    public ResponseEntity<Review> saveReview(@RequestBody ReviewCreateDTO reviewCreateDTO, @RequestHeader(name = "Authorization") String token) {
        if (!userValid(reviewCreateDTO.getUsersid(), token)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        try {
            return ResponseEntity.ok(reviewService.saveReview(reviewCreateDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public boolean userValid(Integer usersid, String token) {
        try {
            token = token.substring(7);
            Token token_ref = tokenRepository.findTokenByToken(token);
            if (token_ref.expired || token_ref.revoked) {
                return false;
            }
            if (token_ref.user.getRole().equals(Role.ADMIN)) {
                return true;
            }
            return token_ref.user.getId().equals(usersid);
        } catch (Exception e) {
            return false;
        }
    }
}
