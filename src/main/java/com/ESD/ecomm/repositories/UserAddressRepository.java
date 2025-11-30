package com.ESD.ecomm.repositories;
import com.ESD.ecomm.entities.UserAddress;
import com.ESD.ecomm.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    List<UserAddress> findByUser(User user);

    Optional<UserAddress> findByUserAndIsDefaultTrue(User user);

    boolean existsByUser(User user);

    // Find addresses by type (HOME, WORK, etc.) for a user like Enums ko use karke
    List<UserAddress> findByUserAndAddressType(User user, com.ESD.ecomm.enums.AddressType addressType);
}

