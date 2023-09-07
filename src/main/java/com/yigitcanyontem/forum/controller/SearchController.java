package com.yigitcanyontem.forum.controller;

import com.yigitcanyontem.forum.model.entertainment.*;
import com.yigitcanyontem.forum.service.*;
import com.yigitcanyontem.forum.entity.Users;
import com.yigitcanyontem.forum.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//TODO add to frontend
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {
    private final UsersService usersService;
    private final ShowService showService;
    private final BookService bookService;
    private final AlbumService albumService;
    private final MovieService movieService;
    private final GameService gameService;


    @GetMapping("show/{title}")
    @Cacheable(value = "searched_shows", key = "'title-'+#title")
    public ResponseEntity<List<Show>> getShowSearchResults(@PathVariable String title) {
        try {
            return ResponseEntity.ok(showService.getShowSearchResults(title));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/album/{title}")
    @Cacheable(value = "searched_albums", key = "'title-'+#title")
    public ResponseEntity<List<Album>> getAlbumSearchResults(@PathVariable String title) {
        try {
            return ResponseEntity.ok(albumService.getAlbumSearchResults(title));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/book/{title}")
    @Cacheable(value = "searched_books", key = "'title-'+#title")
    public ResponseEntity<List<Book>> getBookSearchResults(@PathVariable String title) {
        try {
            return ResponseEntity.ok(bookService.getBookSearchResults(title));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/movie/{title}")
    @Cacheable(value = "searched_movies", key = "'title-'+#title")
    public ResponseEntity<List<Movie>> getMovieSearchResults(@PathVariable String title) {
        try {
            return ResponseEntity.ok(movieService.getMovieSearchResults(title));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{username}")
    @Cacheable(value = "searched_users", key = "'username-'+#username")
    public ResponseEntity<List<Users>> getUserSearchResults(@PathVariable String username) {
        try {
            return ResponseEntity.ok(usersService.usersList(username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/game/{title}")
    @Cacheable(value = "searched_games", key = "'title-'+#title")
    public ResponseEntity<List<GameSearchDTO>> getGameSearchResults(@PathVariable String title) {
        try {
            return ResponseEntity.ok(gameService.getGameSearchResults(title));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
