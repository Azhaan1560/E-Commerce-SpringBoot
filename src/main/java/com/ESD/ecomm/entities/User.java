package com.ESD.ecomm.entities;
import jakarta.persistence.*;
import lombok.*;
import com.ESD.ecomm.enums.UserRoles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Users",
        indexes={
            @Index(name="users_idx_email",columnList="email"),
            @Index(name="users_idx_username",columnList="username")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @Column(nullable = false, unique = true, length=50)
    private String username;

    @Column(nullable = false, unique = true, length=100)
    private String email;

    @Column(nullable = false,length=255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name="user_role",nullable = false)
    private UserRoles userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAddress> addresses = new ArrayList<>();

    @Column(name="first_name",nullable = false,length=50)
    private String firstname;

    @Column(name="last_name",length=50)
    private String lastname;

    @Column(name="phone_number",nullable = false,length=11)
    private String phoneNumber;

    @Column(name="is_active")
    private boolean active=true; // Default values set in Java to ensure new User objects have proper initial state
                                   //Otherwise NULL will be sent to DB which will cause issue when creating new users
    @Column(name="is_email_verified")
    private boolean emailVerified=false;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @PrePersist //Runs Before Entity is saved for the first
                //Dono ki values set ho jain gi createdAt aur updatedAt ki
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate //Runs before the entity is updated in DB
               //Last modified hai basically, jab bhi update karein ge time save ho jai ga
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
