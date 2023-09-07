package com.yigitcanyontem.forum.mapper;

import com.yigitcanyontem.forum.entity.Review;
import com.yigitcanyontem.forum.model.review.ReviewDTO;
import com.yigitcanyontem.forum.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReviewDTOMapper implements Function<Review, ReviewDTO> {
    private final UsersService usersService;

    @Override
    public ReviewDTO apply(Review review) {

        return new ReviewDTO(
                review.getId(),
                usersService.getUserModel(review.getUsersid().getId()),
                review.getEntertainmentType(),
                review.getEntertainmentid(),
                review.getEntertainmentTitle(),
                review.getDescription(),
                review.getTitle(),
                review.getRating(),
                review.getUpvote(),
                review.getDownvote(),
                review.getDate(),
                review.getEntertainmentImage()
        );
    }
}