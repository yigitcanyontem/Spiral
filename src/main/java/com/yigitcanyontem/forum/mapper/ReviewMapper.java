package com.yigitcanyontem.forum.mapper;

import com.yigitcanyontem.forum.entity.Review;
import com.yigitcanyontem.forum.model.review.ReviewCreateDTO;
import com.yigitcanyontem.forum.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReviewMapper implements Function<ReviewCreateDTO, Review> {
    private final UsersService usersService;

    @Override
    public Review apply(ReviewCreateDTO reviewCreateDTO) {
        return new Review(
                usersService.getUser(reviewCreateDTO.getUsersid()),
                reviewCreateDTO.getEntertainmentType(),
                reviewCreateDTO.getEntertainmentid(),
                reviewCreateDTO.getDescription(),
                reviewCreateDTO.getTitle(),
                reviewCreateDTO.getRating(),
                0,
                0,
                Date.valueOf(LocalDate.now(ZoneId.of("Europe/Istanbul")))
        );
    }
}


