package com.yigitcanyontem.forum.service;

import com.yigitcanyontem.forum.entity.Review;
import com.yigitcanyontem.forum.entity.enums.EntertainmentType;
import com.yigitcanyontem.forum.mapper.ReviewDTOMapper;
import com.yigitcanyontem.forum.mapper.ReviewMapper;
import com.yigitcanyontem.forum.model.review.*;
import com.yigitcanyontem.forum.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ReviewDTOMapper reviewDTOMapper;
    private final ReviewReactionService reviewReactionService;

    public PaginatedReviewDTO getReviewsByUser(Integer usersid_id, int pageNumber, int pageSize, String sort, String direction,@Min(1) @Max(5) Integer rating){
        Pageable pageable = reviewSort(pageNumber,pageSize,sort,direction);
        Page<Review> reviewPage;
        if (rating != null && rating >= 0 && rating <= 5){
            reviewPage = reviewRepository.findReviewsByUsersid_IdAndRating(usersid_id,rating,pageable);
        }else {
            reviewPage = reviewRepository.findReviewsByUsersid_Id(usersid_id,pageable);
        }
        return new PaginatedReviewDTO(
                reviewPage.getContent().stream().map(reviewDTOMapper).toList(),
                reviewPage.getTotalPages()
        );
    }
    public PaginatedReview getReviewsByEntertainment(EntertainmentType entertainmentType, String entertainmentId, int pageNumber, int pageSize, String sort, String direction, @Min(1) @Max(5) Integer rating){
        Pageable pageable = reviewSort(pageNumber,pageSize,sort,direction);
        Page<Review> reviewPage;
        if (rating != null && rating >= 0 && rating <= 5){
            reviewPage = reviewRepository.findReviewsByEntertainmentTypeAndEntertainmentidAndRating(entertainmentType,entertainmentId,rating,pageable);
        }else {
            reviewPage = reviewRepository.findReviewsByEntertainmentTypeAndEntertainmentid(entertainmentType,entertainmentId,pageable);
        }
        return new PaginatedReview(
                reviewPage.getContent().stream().map(reviewDTOMapper).toList(),
                reviewPage.getTotalPages()
        );
    }

    public ReviewDTO getReviewsDTOById(Long id){
        return reviewDTOMapper.apply(reviewRepository.findReviewById(id));
    }

    public Review getReviewsById(Long id){
        return reviewRepository.findReviewById(id);
    }



    public Double getAverageRatingByEntertainment(EntertainmentType entertainmentType, String entertainmentId){
        return reviewRepository.findReviewsByEntertainmentTypeAndEntertainmentid(entertainmentType,entertainmentId)
                .stream()
                .mapToDouble(Review::getRating).average().orElse(0);
    }

    public Pageable reviewSort(int pageNumber, int pageSize,String sort, String direction){
        if (sort != null && direction != null && (sort.equals("rating") || sort.equals("upvote") || sort.equals("downvote") || sort.equals("date") || sort.equals("id"))){
            if (direction.equals("ASC")){
                return PageRequest.of(pageNumber, pageSize, Sort.by(sort).ascending());
            }else if (direction.equals("DSC")){
                return PageRequest.of(pageNumber, pageSize, Sort.by(sort).descending());
            }
        }
        return PageRequest.of(pageNumber, pageSize);
    }


    @Transactional
    public Review updateReview(ReviewUpdateDTO reviewUpdateDTO) throws Exception {
        Review review = reviewRepository.getReferenceById(reviewUpdateDTO.getId());
        String description = reviewUpdateDTO.getDescription();
        String title = reviewUpdateDTO.getTitle();
        Integer rating = reviewUpdateDTO.getRating();
        if (title != null){
            review.setTitle(title);
        }

        if (description != null){
            review.setDescription(description);
        }

        if (rating != null){
            review.setRating(rating);
        }
        return reviewRepository.save(review);
    }


    public Review saveReview(ReviewCreateDTO reviewCreateDTO) {
        Review review = reviewMapper.apply(reviewCreateDTO);
        return reviewRepository.save(review);
    }

    @Transactional
    public String deleteReview(Long id) {
        try {
            reviewReactionService.deleteReactionsOfReview(reviewRepository.findReviewById(id));
            reviewRepository.deleteById(id);
        }catch (Exception e){
            return "Fail";
        }
        return "Success";
    }

}
