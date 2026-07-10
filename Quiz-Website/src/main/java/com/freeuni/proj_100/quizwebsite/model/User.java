package com.freeuni.proj_100.quizwebsite.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Represents a user entity within the application.
 * This class maps to the "users" table in the database and integrates with 
 * Spring Security by implementing the {@link UserDetails} interface.
 */
@Entity
@Table(name="users")
public class User implements UserDetails {

    /**
     * The unique identifier for the user. Automatically generated.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    /**
     * The unique username used for authentication. Maximum length of 50 characters.
     */
    @Column(nullable=false, unique=true, length=50)
    private String username;

    /**
     * The unique email address associated with the user account.
     */
    @Column(nullable=false, unique=true, length=100)
    private String email;

    /**
     * The secure, hashed version of the user's password.
     */
    @Column(nullable=false)
    private String passwordHash;

    /**
     * The timestamp indicating when the user record was created.
     * This value cannot be updated after creation.
     */
    @Column(nullable=false, updatable=false)
    private LocalDateTime createdAt;

    /**
     * Flag indicating whether the user has administrative privileges.
     * Defaults to false.
     */
    @Column(nullable=false)
    private boolean isAdmin = false;

    @Column(nullable=false)
    private int tokenVersion = 0;

    public User() {}

    /**
     * Constructs a new User with all fields initialized.
     *
     * @param id           The unique identifier for the user
     * @param username     The unique username
     * @param email        The unique email address
     * @param passwordHash The hashed password
     * @param createdAt    The creation timestamp
     * @param isAdmin      The administrative status flag
     */
    public User(Integer id, String username, String email, String passwordHash,
                LocalDateTime createdAt, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.isAdmin = isAdmin;
    }

    /**
     *  Promotes the user to admin by setting property and
     *  incrementing version number which invalidates the previous token
     */
    public void promoteToAdmin() {
        this.isAdmin = true;
        this.tokenVersion++;
    }

    /**
     * JPA lifecycle callback method that automatically sets the {@code createdAt}
     * timestamp to the current date and time before the entity is persisted.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Integer getId() { return id; }
    public String getEmail() { return email; }
    public boolean isAdmin() { return isAdmin; }    // JPA expects 'isAdmin' and not 'getIsAdmin'
    public int getTokenVersion() { return tokenVersion; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Integer id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    /* -- UserDetails interface -- */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = isAdmin ? "ROLE_ADMIN" : "ROLE_USER";
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() { return username; }

    @Override
    public String getPassword() { return passwordHash; }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return {@code true} always, as account expiration is not implemented
     */
    @Override
    public boolean isAccountNonExpired() { return true; }

    /**
     * Indicates whether the user is locked or unlocked.
     *
     * @return {@code true} always, as account locking is not implemented
     */
    @Override
    public boolean isAccountNonLocked() { return true; }

    /**
     * Indicates whether the user's credentials (password) have expired.
     *
     * @return {@code true} always, as credentials expiration is not implemented
     */
    @Override
    public boolean isCredentialsNonExpired() { return true; }

    /**
     * Indicates whether the user is enabled or disabled.
     *
     * @return {@code true} always, as user disabling is not implemented
     */
    @Override
    public boolean isEnabled() { return true; }
}