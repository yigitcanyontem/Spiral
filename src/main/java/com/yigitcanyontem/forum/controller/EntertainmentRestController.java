package com.yigitcanyontem.forum.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yigitcanyontem.forum.entity.Country;
import com.yigitcanyontem.forum.model.entertainment.*;
import com.yigitcanyontem.forum.service.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/entertainment")
public class EntertainmentRestController {
    private final ShowService showService;
    private final BookService bookService;
    private final AlbumService albumService;
    private final MovieService movieService;
    private final GameService gameService;

    public EntertainmentRestController(ShowService showService, BookService bookService, AlbumService albumService, MovieService movieService, GameService gameService) {
        this.showService = showService;
        this.bookService = bookService;
        this.albumService = albumService;
        this.movieService = movieService;
        this.gameService = gameService;
    }

    @GetMapping("/show/{showid}")
    @Cacheable(value = "shows", key = "'show-' + #showid")
    public ResponseEntity<Show> getSingleShowById(@PathVariable Integer showid) {
        try {
            return ResponseEntity.ok(showService.getSingleShowById(showid));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/movie/{movieid}")
    @Cacheable(value = "movies", key = "'movie-' + #movieid")
    public ResponseEntity<Movie> getSingleMovieById(@PathVariable Integer movieid) {
        try {
            return ResponseEntity.ok(movieService.getSingleMovieById(movieid));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/book/{bookid}")
    @Cacheable(value = "books", key = "'book-' + #bookid")
    public ResponseEntity<Book> getSingleBookById(@PathVariable String bookid) throws JsonProcessingException {
        return ResponseEntity.ok(bookService.getSingleBookById(bookid));

    }

    @GetMapping("/album/{mbid}")
    @Cacheable(value = "albums", key = "'album-' + #mbid")
    public ResponseEntity<Album> getSingleAlbumById(@PathVariable String mbid) {
        try {
            return ResponseEntity.ok(albumService.getSingleAlbumById(mbid));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/game/{gameid}")
    @Cacheable(value = "games", key = "'game-' + #gameid")
    public ResponseEntity<Game> getSingleGameById(@PathVariable String gameid) {
        try {
            return ResponseEntity.ok(gameService.getSingleGameById(gameid));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/crew/{id}")
    @Cacheable(value = "crew", key = "'crew-' + #id")
    public ResponseEntity<CrewMember> getSingleCrewMemberById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(movieService.getCrewMember(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
