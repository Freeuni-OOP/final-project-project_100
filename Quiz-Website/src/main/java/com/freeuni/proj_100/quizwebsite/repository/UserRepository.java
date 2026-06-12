package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 * Provides standard CRUD operations inherited from {@link JpaRepository} 
 * alongside custom query methods for authentication and validation.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Retrieves a user by their unique username.
     *
     * @param username the username of the user to look up
     * @return an {@link Optional} containing the found user, or empty if no user matches
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user already exists with the given username.
     *
     * @param username the username to check for existence
     * @return {@code true} if a user with the username exists, {@code false} otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user already exists with the given email address.
     *
     * @param email the email address to check for existence
     * @return {@code true} if a user with the email exists, {@code false} otherwise
     */
    boolean existsByEmail(String email);
}
