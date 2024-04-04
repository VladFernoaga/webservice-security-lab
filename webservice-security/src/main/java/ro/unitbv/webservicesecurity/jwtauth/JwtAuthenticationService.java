package ro.unitbv.webservicesecurity.jwtauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserDetailsService userDetailsService;

    public String login(String username, String password) throws JwtAuthFailedException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            var currentUser = userDetailsService.loadUserByUsername(username);
            return jwtTokenService.generateToken(currentUser);
        } catch (DisabledException e) {
            throw new JwtAuthFailedException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new JwtAuthFailedException("INVALID_CREDENTIALS", e);
        }catch (AuthenticationException e){
            throw new JwtAuthFailedException(null, e);
        }
    }
}
