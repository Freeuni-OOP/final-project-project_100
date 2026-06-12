package com.freeuni.proj_100.quizwebsite.security;

import com.freeuni.proj_100.quizwebsite.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service implementation for loading user-specific data within Spring Security.
 * This class implements the {@link UserDetailsService} interface to bridge 
 * the application's {@link UserRepository} database queries with Spring's 
 * authentication mechanisms.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    /**
     * Data access object utilized to query user accounts from the database.
     */
    private final UserRepository userRepo;

    /**
     * Constructs a new {@code UserDetailsServiceImpl} with the required repository dependency.
     *
     * @param repo the user repository instance injected by Spring
     */
    public UserDetailsServiceImpl(UserRepository repo) {
        this.userRepo = repo;
    }

    /**
     * Locates the user based on the provided username.
     * Spring Security calls this method during authentication to retrieve credentials 
     * and authority roles for verification.
     *
     * @param username the username identifying the user whose data is required
     * @return a fully populated {@link UserDetails} principal object
     * @throws UsernameNotFoundException if the user could not be found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Couldn't find user: " + username
                ));
    }
}
