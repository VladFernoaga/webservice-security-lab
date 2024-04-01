package ro.unitbv.webservicesecurity.config.basicauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
//@EnableWebSecurity
public class BasicAuthSecurityConfig {
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
            .anyRequest()
            .denyAll())
          .authenticationManager(authenticationManager)
          .httpBasic((c) -> c.init(http));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
        var authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService)
          .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admin")
          .password(passwordEncoder.encode("admin"))
          .roles("READ", "EDIT", "DELETE")
          .build());
        manager.createUser(User.withUsername("user")
          .password(passwordEncoder.encode("user"))
          .roles("READ")
          .build());
        manager.createUser(User.withUsername("editor")
          .password(passwordEncoder.encode("editor"))
          .roles("READ", "EDIT")
          .build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
