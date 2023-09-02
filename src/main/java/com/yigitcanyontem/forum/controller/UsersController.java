package com.yigitcanyontem.forum.controller;

import com.yigitcanyontem.forum.entity.*;
import com.yigitcanyontem.forum.entity.enums.Role;
import com.yigitcanyontem.forum.model.entertainment.*;
import com.yigitcanyontem.forum.model.socialmedia.SocialMediaDTO;
import com.yigitcanyontem.forum.model.users.UserDTO;
import com.yigitcanyontem.forum.repository.TokenRepository;
import com.yigitcanyontem.forum.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;
    private final DescriptionService descriptionService;
    private final SocialMediaService socialMediaService;
    private final FavBooksService favBooksService;
    private final FavShowsService favShowsService;
    private final FavAlbumsService favAlbumsService;
    private final FavMovieService favMovieService;
    private final FavGameService favGameService;
    private final TokenRepository tokenRepository;
    private final MovieService movieService;
    private final ShowService showService;
    private final AlbumService albumService;
    private final GameService gameService;
    private final BookService bookService;


    @GetMapping("/{id}")
    @Cacheable(value = "user", key = "'id-' + #id")
    public ResponseEntity<UserDTO> getUser(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(usersService.getUserModel(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<Users> getCustomer(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(usersService.getUser(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/socialmedia/{id}")
    @Cacheable(value = "socialmedia", key = "'id-' + #id")
    public ResponseEntity<SocialMediaDTO> getCustomerSocialMedia(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(socialMediaService.getSocialMediaDTO(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/description/{id}")
    @Cacheable(value = "description", key = "'id-' + #id")
    public ResponseEntity<Description> getCustomerDescription(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(descriptionService.description(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PatchMapping("/update")
    @Caching(evict = {
            @CacheEvict(value = "user", key = "'id-' + #assignModel.id"),
            @CacheEvict(value = "images", key = "'id-' + #assignModel.id"),
            @CacheEvict(value = "description", key = "'id-' + #assignModel.id"),
            @CacheEvict(value = "socialmedia", key = "'id-' + #assignModel.id")
    })
    public ResponseEntity<List<String>> updateCustomer(@RequestBody AssignModel assignModel,@RequestHeader (name="Authorization") String token) {
        if (!userValid(assignModel.getId(),token)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        try {
            return ResponseEntity.ok(usersService.updateCustomer(assignModel));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DeleteMapping("/delete/{usersid}")
    @CacheEvict(value = "user", key = "'id-' + #usersid")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> deleteCustomer(@PathVariable Integer usersid,@RequestHeader (name="Authorization") String token) {
        if (!userValid(usersid,token)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        try {
            usersService.deleteCustomer(usersid);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/favmovie/{id}")
    @Cacheable(value = "favmovie", key = "'id-' + #id")
    public ResponseEntity<List<Movie>> getFavMovies(@PathVariable Integer id) {
        try {
            List<FavMovie> movieids = favMovieService.findByUserId(getCustomer(id).getBody());
            return ResponseEntity.ok(movieService.getMovieTOByUser(movieids));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/favshows/{id}")
    @Cacheable(value = "favshows", key = "'id-' + #id")
    public ResponseEntity<List<Show>> getFavShows(@PathVariable Integer id) {

        try {
            List<FavShows> showids = favShowsService.findByUserId(getCustomer(id).getBody());
            return ResponseEntity.ok(showService.getShowDTOByUser(showids));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/favalbums/{id}")
    @Cacheable(value = "favalbums", key = "'id-' + #id")
    public ResponseEntity<List<Album>> getFavAlbums(@PathVariable Integer id) {
        try {
            List<FavAlbums> albumids = favAlbumsService.findByUserId(getCustomer(id).getBody());
            return ResponseEntity.ok(albumService.getAlbumDTOsByUser(albumids));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/favbooks/{id}")
    @Cacheable(value = "favbooks", key = "'id-' + #id")
    public ResponseEntity<List<Book>> getFavBooks(@PathVariable Integer id) {
        try {
            List<FavBooks> bookids = favBooksService.findByUserId(getCustomer(id).getBody());
            return ResponseEntity.ok(bookService.getGameDTOsByUser(bookids));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/favgames/{id}")
    @Cacheable(value = "favgames", key = "'id-' + #id")
    public ResponseEntity<List<Game>> getFavGames(@PathVariable Integer id) {
        try {
            List<FavGame> gameids = favGameService.findByUserId(getCustomer(id).getBody());
            List<Game> games = new ArrayList<>();
            for (FavGame x : gameids) {
                games.add(gameService.getSingleGameDTOById(x.getGameid()));
            }
            return ResponseEntity.ok(games);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/favmovie/{usersid}/{movieid}")
    @Caching(evict = {
            @CacheEvict(value = "favmovie", key = "'id-' + #usersid"),
            @CacheEvict(value = "movies", key = "'movie-' + #movieid")
    })
    public ResponseEntity<?> saveFavMovies(@PathVariable Integer usersid, @PathVariable Integer movieid,@RequestHeader (name="Authorization") String token) {
        if (!userValid(usersid,token)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        try {
            favMovieService.saveFavMovie(Objects.requireNonNull(getCustomer(usersid).getBody()), movieid);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/favgames/{usersid}/{gameid}")
    @Caching(evict = {
            @CacheEvict(value = "favgames", key = "'id-' + #usersid"),
            @CacheEvict(value = "games", key = "'game-' + #gameid")
    })
    public ResponseEntity<?> saveFavGames(@PathVariable Integer usersid, @PathVariable String gameid,@RequestHeader (name="Authorization") String token) {
        if (!userValid(usersid,token)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        try {
            favGameService.saveFavGame(Objects.requireNonNull(getCustomer(usersid).getBody()), gameid);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/favshows/{usersid}/{showid}")
    @Caching(evict = {
            @CacheEvict(value = "favshows", key = "'id-' + #usersid"),
            @CacheEvict(value = "shows", key = "'show-' + #showid")
    })
    public ResponseEntity<?> saveFavShows(@PathVariable Integer usersid, @PathVariable Integer showid,@RequestHeader (name="Authorization") String token) {
        if (!userValid(usersid,token)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        try {
            favShowsService.saveFavShows(Objects.requireNonNull(getCustomer(usersid).getBody()), showid);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/favalbums/{usersid}/{albumid}")
    @Caching(evict = {
            @CacheEvict(value = "favalbums", key = "'id-' + #usersid"),
            @CacheEvict(value = "albums", key = "'album-' + #albumid")
    })
    public ResponseEntity<?> saveFavAlbums(@PathVariable Integer usersid, @PathVariable String albumid,@RequestHeader (name="Authorization") String token) {
        if (!userValid(usersid,token)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        try {
            favAlbumsService.saveFavAlbums(Objects.requireNonNull(getCustomer(usersid).getBody()), albumid);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/favbooks/{usersid}/{bookid}")
    @Caching(evict = {
            @CacheEvict(value = "favbooks", key = "'id-' + #usersid"),
            @CacheEvict(value = "books", key = "'book-' + #bookid")
    })
    public ResponseEntity<?> saveFavBooks(@PathVariable Integer usersid, @PathVariable String bookid,@RequestHeader (name="Authorization") String token) {
        if (!userValid(usersid,token)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        try {
            favBooksService.saveFavBooks(Objects.requireNonNull(getCustomer(usersid).getBody()), bookid);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/favmovie/delete/{usersid}/{movieid}")
    @Caching(evict = {
            @CacheEvict(value = "favmovie", key = "'id-' + #usersid"),
            @CacheEvict(value = "movies", key = "'movie-' + #movieid")
    })
    public ResponseEntity<?> deleteFavMovies(@PathVariable Integer usersid, @PathVariable Integer movieid,@RequestHeader (name="Authorization") String token) {
        if (!userValid(usersid,token)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        try {
            favMovieService.deleteUserFavMovieById(getCustomer(usersid).getBody(), movieid);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/favshows/delete/{usersid}/{showid}")
    @Caching(evict = {
            @CacheEvict(value = "favshows", key = "'id-' + #usersid"),
            @CacheEvict(value = "shows", key = "'show-' + #showid")
    })
    public ResponseEntity<?> deleteFavShows(@PathVariable Integer usersid, @PathVariable Integer showid,@RequestHeader (name="Authorization") String token) {
        if (!userValid(usersid,token)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        try {
            favShowsService.deleteUserFavShowsById(getCustomer(usersid).getBody(), showid);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/favalbums/delete/{usersid}/{albumid}")
    @Caching(evict = {
            @CacheEvict(value = "favalbums", key = "'id-' + #usersid"),
            @CacheEvict(value = "albums", key = "'album-' + #albumid")
    })
    public ResponseEntity<?> deleteFavAlbums(@PathVariable Integer usersid, @PathVariable String albumid,@RequestHeader (name="Authorization") String token) {
        if (!userValid(usersid,token)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        try {
            favAlbumsService.deleteUserFavAlbumsById(getCustomer(usersid).getBody(), albumid);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/favbooks/delete/{usersid}/{bookid}")
    @Caching(evict = {
            @CacheEvict(value = "favbooks", key = "'id-' + #usersid"),
            @CacheEvict(value = "books", key = "'book-' + #bookid")
    })
    public ResponseEntity<?> deleteFavBooks(@PathVariable Integer usersid, @PathVariable String bookid,@RequestHeader (name="Authorization") String token) {
        if (!userValid(usersid,token)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        try {
            favBooksService.deleteUserFavBooksById(getCustomer(usersid).getBody(), bookid);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/favgames/delete/{usersid}/{gameid}")
    @Caching(evict = {
            @CacheEvict(value = "favgames", key = "'id-' + #usersid"),
            @CacheEvict(value = "games", key = "'game-' + #gameid")
    })
    public ResponseEntity<?> deleteFavGames(@PathVariable Integer usersid, @PathVariable String gameid,@RequestHeader (name="Authorization") String token) {
        if (!userValid(usersid,token)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        try {
            favGameService.deleteUserFavGameById(getCustomer(usersid).getBody(), gameid);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/upload/{id}")
    @CacheEvict(value = "images", key = "'id-' + #id")
    public ResponseEntity<List<String>> uploadImage(@RequestParam("file") MultipartFile file, @PathVariable Integer id,@RequestHeader (name="Authorization") String token) {
        if (!userValid(id,token)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        try {
            return ResponseEntity.ok(usersService.uploadPicture(file, id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/images/{id}")
    @Cacheable(value = "images", key = "'id-' + #id")
    public ResponseEntity<Resource> getImage(@PathVariable Integer id) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return ResponseEntity.ok().headers(headers).body(usersService.getImage(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public boolean userValid(Integer usersid, String token){
        try {
            token = token.substring(7);
            Token token_ref = tokenRepository.findTokenByToken(token);
            if (token_ref.expired || token_ref.revoked){
                return false;
            }
            if (token_ref.user.getRole().equals(Role.ADMIN)){
                return true;
            }
            return token_ref.user.getId().equals(usersid);
        }catch (Exception e){
            return false;
        }
    }

    @PostMapping("/check")
    public ResponseEntity<String> checkToken() {
        try {
            return ResponseEntity.ok("VALID");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
