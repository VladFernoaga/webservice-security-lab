package ro.unitbv.webservicesecurity.config.jwt;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ro.unitbv.webservicesecurity.jwtauth.JwtTokenService;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER_PREFIX = "Bearer ";
    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final var userDetails = extractToken(request.getHeader("Authorization")).flatMap(this::getUserDetails);

        if (userDetails.isPresent() && notAuthenticatedInCurrentContext()) {
            var usernamePasswordAuthenticationToken = createUsernameAutheticationToken(request, userDetails);

            SecurityContextHolder.getContext()
              .setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    private static UsernamePasswordAuthenticationToken createUsernameAutheticationToken(HttpServletRequest request, Optional<UserDetails> userDetails) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails.get(), null, userDetails.get()
          .getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return usernamePasswordAuthenticationToken;
    }

    private static boolean notAuthenticatedInCurrentContext() {
        return SecurityContextHolder.getContext()
          .getAuthentication() == null;
    }

    private Optional<UserDetails> getUserDetails(String jwtToken) {
        return Optional.ofNullable(jwtTokenService.getUsernameFromToken(jwtToken))
          .map(username -> this.userDetailsService.loadUserByUsername(username))
          .filter(userDetails -> jwtTokenService.validateToken(jwtToken, userDetails));
    }

    private Optional<String> extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(AUTH_HEADER_PREFIX)) {
            var jwt = authorizationHeader.substring(7);
            return jwt.isEmpty() ? Optional.empty() : Optional.of(jwt);
        }
        return Optional.empty();
    }
}
