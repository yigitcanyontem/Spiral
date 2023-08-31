package com.yigitcanyontem.forum.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigitcanyontem.forum.model.auth.AuthenticationRequest;
import com.yigitcanyontem.forum.model.auth.AuthenticationResponse;
import com.yigitcanyontem.forum.model.auth.RegisterRequest;
import com.yigitcanyontem.forum.exceptions.LoginException;
import com.yigitcanyontem.forum.entity.Token;
import com.yigitcanyontem.forum.repository.TokenRepository;
import com.yigitcanyontem.forum.entity.enums.TokenType;
import com.yigitcanyontem.forum.entity.enums.Role;
import com.yigitcanyontem.forum.entity.enums.Status;
import com.yigitcanyontem.forum.entity.Users;
import com.yigitcanyontem.forum.repository.UsersRepository;
import com.yigitcanyontem.forum.entity.SocialMedia;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UsersRepository repository;
  private final CountryService countryService;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UsersRepository usersRepository;
  private final DescriptionService descriptionService;
  private final FavMovieService favMovieService;
  private final FavAlbumsService favAlbumsService;
  private final FavShowsService favShowsService;
  private final FavBooksService favBooksService;
  private final SocialMediaService socialMediaService;
  private final FavGameService favGameService;

  public AuthenticationResponse register(RegisterRequest request) {
    var user = Users.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .date_of_birth(request.getDate_of_birth())
        .country(countryService.singleCountry(request.getCountry()))
        .email(request.getEmail())
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .status(Status.ACTIVE)
        .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    Users users1 = usersRepository.getUsersByUsername(user.getUsername());
    socialMediaService.saveSocialMedia(new SocialMedia(users1,"user","user","user","user"));
    descriptionService.saveDescription(users1.getId(),"");
    favAlbumsService.saveFavAlbums(users1,"bc5a0db2-a123-4a29-bb75-de01c52da293");
    favBooksService.saveFavBooks(users1,"vrpPEAAAQBAJ");
    favMovieService.saveFavMovie(users1,550);
    favShowsService.saveFavShows(users1,100);
    favGameService.saveFavGame(users1,"3030-89187");
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .id(repository.getUsersByUsername(request.getUsername()).getId())
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        )
    );
    var user = repository.getUsersByUsername(request.getUsername());
    if (user.getStatus().equals(Status.INACTIVE)){
      throw new LoginException("Account doesn't exist");
    }
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
              .id(repository.getUsersByUsername(request.getUsername()).getId())
        .build();
  }

  private void saveUserToken(Users user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(Users user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String username;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    username = jwtService.extractUsername(refreshToken);
    if (username != null) {
      var user = this.repository.getUsersByUsername(username);
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
