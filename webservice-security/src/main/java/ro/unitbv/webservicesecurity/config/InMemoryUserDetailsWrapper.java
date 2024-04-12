package ro.unitbv.webservicesecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class InMemoryUserDetailsWrapper implements  UserDetailsService{

    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

    public InMemoryUserDetailsWrapper(PasswordEncoder passwordEncoder) {
        this.inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername("admin")
          .password(passwordEncoder.encode("admin"))
          .roles("READ", "EDIT", "DELETE")
          .build());
        inMemoryUserDetailsManager.createUser(User.withUsername("user")
          .password(passwordEncoder.encode("user"))
          .roles("READ")
          .build());
        inMemoryUserDetailsManager.createUser(User.withUsername("editor")
          .password(passwordEncoder.encode("editor"))
          .roles("READ", "EDIT")
          .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return inMemoryUserDetailsManager.loadUserByUsername(username);
    }
}
