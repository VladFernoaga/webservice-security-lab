package ro.unitbv.webservicesecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.Valid;
import ro.unitbv.webservicesecurity.dto.jwt.JwtTokenDto;
import ro.unitbv.webservicesecurity.dto.jwt.LoginRequestDto;

@Controller
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/auth/login")
    public ResponseEntity<JwtTokenDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password()));
            return ResponseEntity.ok( new JwtTokenDto("abcv"));
        } catch (DisabledException e) {
            throw new JwtAuthFailedException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new JwtAuthFailedException("INVALID_CREDENTIALS", e);
        }catch (AuthenticationException e){
            throw new JwtAuthFailedException(null, e);
        }

    }
    @ExceptionHandler(JwtAuthFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> validationRequestBodyNotPresentExceptionHandler(JwtAuthFailedException ex) {
        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.causeType);
    }

}

class JwtAuthFailedException extends RuntimeException{
    String causeType;
    public JwtAuthFailedException(String causeType, Throwable cause) {
        super(cause);
        this.causeType = causeType;
    }
}
