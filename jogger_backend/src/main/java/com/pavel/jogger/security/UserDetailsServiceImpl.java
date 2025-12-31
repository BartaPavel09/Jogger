package com.pavel.jogger.security;

import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.persistence.repository.RunnerRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Custom implementation of Spring Security's UserDetailsService.
 * <p>
 * This service is used automatically by the AuthenticationManager during the login process.
 * Its sole purpose is to bridge the gap between the database (RunnerEntity) and
 * Spring Security's internal user representation (UserDetails).
 * </p>
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final RunnerRepository runnerRepository;

    public UserDetailsServiceImpl(RunnerRepository runnerRepository) {
        this.runnerRepository = runnerRepository;
    }

    /**
     * Loads a user by their username.
     * <p>
     * When authenticationManager.authenticate() is called in AuthService, Spring calls this method
     * behind the scenes to find the user in the database.
     * </p>
     * @param username The username identifying the user whose data is required.
     * @return A fully populated UserDetails object (contains username, password hash, roles).
     * @throws UsernameNotFoundException If the user cannot be found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        RunnerEntity runner = runnerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        
        String role = "ROLE_" + runner.getRole();

        return new User(
                runner.getUsername(),
                runner.getPasswordHash(),
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}
