package com.yigitcanyontem.forum.mapper;

import com.yigitcanyontem.forum.entity.Review;
import com.yigitcanyontem.forum.entity.enums.EntertainmentType;
import com.yigitcanyontem.forum.model.entertainment.*;
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
        String image = "";
        String entertainmentTitle = "";
        EntertainmentType entertainmentType = review.getEntertainmentType();
        String entertainmentid = review.getEntertainmentId();

        try {
            if (entertainmentType.equals(EntertainmentType.ALBUM)){
                Album album = albumService.getSingleAlbumById(entertainmentid);
                image = album.getImage();
                entertainmentTitle = album.getName();
            } else if (entertainmentType.equals(EntertainmentType.GAME)) {
                Game game = gameService.getSingleGameById(entertainmentid);
                image = game.getOriginal_url();
                entertainmentTitle = game.getName();
            }else if (entertainmentType.equals(EntertainmentType.MOVIE)) {
                Movie movie = movieService.getSingleMovieById(Integer.parseInt(entertainmentid));
                image = movie.getPoster_path();
                entertainmentTitle = movie.getOriginal_title();
            }else if (entertainmentType.equals(EntertainmentType.SHOW)) {
                Show show =  showService.getSingleShowById(Integer.parseInt(entertainmentid));
                image = show.getPoster_path();
                entertainmentTitle = show.getOriginal_title();
            }else if (entertainmentType.equals(EntertainmentType.BOOK)) {
                Book book = bookService.getSingleBookById(entertainmentid);
                image = book.getCover_url();
                entertainmentTitle = book.getTitle();
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
                entertainmentTitle,
                review.getDescription(),
                review.getTitle(),
                review.getRating(),
                review.getUpvote(),
                review.getDownvote(),
                review.getDate(),
                image
        );
    }
}