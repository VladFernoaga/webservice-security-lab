package ro.unitbv.webservicesecurity.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class JwtWebSecurity {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
          .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/product/**")
            .hasRole("READ")
            .requestMatchers(HttpMethod.POST, "/product/**")
            .hasRole("EDIT")
            .requestMatchers(HttpMethod.PATCH, "/product/**")
            .hasRole("EDIT")
            .requestMatchers(HttpMethod.DELETE, "/product/**")
            .hasRole("DELETE")
            .requestMatchers(HttpMethod.POST, "/auth/login/**")
            .permitAll()
            .anyRequest()
            .authenticated())
          .authenticationManager(authenticationManager)
          .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // set JwtRequestFilter before Spring calls the usernamePasswordAuthFilter
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        var authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService)
          .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

}
