package ro.unitbv.webservicesecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.Valid;
import ro.unitbv.webservicesecurity.dto.jwt.JwtTokenDto;
import ro.unitbv.webservicesecurity.dto.jwt.LoginRequestDto;
import ro.unitbv.webservicesecurity.jwtauth.JwtAuthFailedException;
import ro.unitbv.webservicesecurity.jwtauth.JwtAuthenticationService;

@Controller
public class JwtAuthenticationController {

    @Autowired
    private JwtAuthenticationService jwtAuthenticationService;

    @PostMapping("/auth/login")
    public ResponseEntity<JwtTokenDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(new JwtTokenDto(jwtAuthenticationService.login(loginRequestDto.username(), loginRequestDto.password())));
    }

    @ExceptionHandler(JwtAuthFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> validationRequestBodyNotPresentExceptionHandler(JwtAuthFailedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ex.getCauseType());
    }

}

