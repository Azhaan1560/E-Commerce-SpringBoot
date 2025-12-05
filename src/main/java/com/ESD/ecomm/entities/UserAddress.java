package com.ESD.ecomm.entities;
import jakarta.persistence.*;
import lombok.*;
import com.ESD.ecomm.enums.AddressType;

import java.time.LocalDateTime;


@Entity
@Table(name="User_Address",
       indexes = {
               @Index(name = "ua_idx_user", columnList = "user_id"),
               @Index(name="ua_idx_default",columnList = "is_default")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name="address_type", nullable=false, length=10)
    private AddressType addressType = AddressType.HOME;

    @Column(name="full_name", nullable = false, length=100)
    private String fullName;

    @Column(name="phone_number", nullable = false, length=20)
    private String phoneNumber;

    @Column(name="address_line1", nullable = false, length=255)
    private String addressLine1;

    @Column(name="address_line2", length=255)
    private String addressLine2;

    @Column(name="city", nullable = false, length=100)
    private String city;

    @Column(name="state", nullable = false, length=100)
    private String state;

    @Column(name="country", nullable = false, length=100)
    private String country;

    @Column(name="zip_code", nullable = false, length=20)
    private String zipCode;

    @Column(name="is_default")
    private Boolean isDefault = false;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
