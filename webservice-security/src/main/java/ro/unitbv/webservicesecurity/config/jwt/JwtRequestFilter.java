package ro.unitbv.webservicesecurity.config.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final var authorities = extractToken(request.getHeader("Authorization")).flatMap(this::getAuthorities);

        if (authorities.isPresent() && notAuthenticatedInCurrentContext()) {
            var usernamePasswordAuthenticationToken = createUsernameAuthenticationToken(request, authorities.get());
            SecurityContextHolder.getContext()
              .setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    private static UsernamePasswordAuthenticationToken createUsernameAuthenticationToken(HttpServletRequest request, List<GrantedAuthority> authorities) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(null, null, authorities);
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return usernamePasswordAuthenticationToken;
    }

    private static boolean notAuthenticatedInCurrentContext() {
        return SecurityContextHolder.getContext()
          .getAuthentication() == null;
    }

    private Optional<List<GrantedAuthority>> getAuthorities(String jwtToken) {
        if(jwtTokenService.validateToken(jwtToken)){
            return  Optional.ofNullable(jwtTokenService.getRolesFromToken(jwtToken));
        }else {
            return Optional.empty();
        }
    }

    private Optional<String> extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(AUTH_HEADER_PREFIX)) {
            var jwt = authorizationHeader.substring(7);
            return jwt.isEmpty() ? Optional.empty() : Optional.of(jwt);
        }
        return Optional.empty();
    }
}
