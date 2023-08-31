package com.yigitcanyontem.forum.controller;

import com.yigitcanyontem.forum.entity.Country;
import com.yigitcanyontem.forum.model.auth.AuthenticationRequest;
import com.yigitcanyontem.forum.model.auth.AuthenticationResponse;
import com.yigitcanyontem.forum.service.AuthenticationService;
import com.yigitcanyontem.forum.model.auth.RegisterRequest;
import com.yigitcanyontem.forum.service.CountryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;
  private final CountryService countryService;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
    return ResponseEntity.ok(service.register(request));
  }
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }

  @GetMapping("/countries")
  @Cacheable(value = "countries")
  public ResponseEntity<List<Country>> getCountries() {
    try {
      return ResponseEntity.ok(countryService.allCountries());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }



}
