package com.yigitcanyontem.forum.mapper;

import com.yigitcanyontem.forum.entity.Review;
import com.yigitcanyontem.forum.entity.enums.EntertainmentType;
import com.yigitcanyontem.forum.model.entertainment.Entertainment;
import com.yigitcanyontem.forum.model.review.ReviewDTO;
import com.yigitcanyontem.forum.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReviewDTOMapper implements Function<Review, ReviewDTO> {
    private final ShowService showService;
    private final BookService bookService;
    private final AlbumService albumService;
    private final MovieService movieService;
    private final GameService gameService;
    private final UsersService usersService;

    @Override
    public ReviewDTO apply(Review review) {
        Entertainment entertainment = null;
        EntertainmentType entertainmentType = review.getEntertainmentType();
        String entertainmentid = review.getEntertainmentId();

        try {
            if (entertainmentType.equals(EntertainmentType.ALBUM)){
                entertainment = albumService.getSingleAlbumById(entertainmentid);
            } else if (entertainmentType.equals(EntertainmentType.GAME)) {
                entertainment = gameService.getSingleGameById(entertainmentid);
            }else if (entertainmentType.equals(EntertainmentType.MOVIE)) {
                entertainment = movieService.getSingleMovieById(Integer.parseInt(entertainmentid));
            }else if (entertainmentType.equals(EntertainmentType.SHOW)) {
                entertainment = showService.getSingleShowById(Integer.parseInt(entertainmentid));
            }else if (entertainmentType.equals(EntertainmentType.BOOK)) {
                entertainment = bookService.getSingleBookById(entertainmentid);
            }
        }catch (IOException e){
            throw new RuntimeException("");
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        return new ReviewDTO(
                review.getId(),
                usersService.getUserModel(review.getUsersid().getId()),
                entertainmentType,
                entertainmentid,
                review.getDescription(),
                review.getRating(),
                review.getUpvote(),
                review.getDownvote(),
                review.getDate(),
                entertainment
        );
    }
}