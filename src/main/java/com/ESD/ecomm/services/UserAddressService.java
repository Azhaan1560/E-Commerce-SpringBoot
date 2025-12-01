package com.ESD.ecomm.services;
import com.ESD.ecomm.entities.User;
import com.ESD.ecomm.entities.UserAddress;
import com.ESD.ecomm.enums.AddressType;
import com.ESD.ecomm.repositories.UserAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserAddressService {

    private final UserAddressRepository userAddressRepository;

    @Autowired
    public UserAddressService(UserAddressRepository userAddressRepository) {
        this.userAddressRepository = userAddressRepository;
    }

    public List<UserAddress> getAddressesByUser(User user) {
        return userAddressRepository.findByUser(user);
    }

    public Optional<UserAddress> getDefaultAddress(User user) {
        return userAddressRepository.findByUserAndIsDefaultTrue(user);
    }

    public UserAddress saveAddress(UserAddress address) {
        return userAddressRepository.save(address);
    }

    public boolean existsByUser(User user) {
        return userAddressRepository.existsByUser(user);
    }

    public List<UserAddress> getAddressesByType(User user, AddressType addressType) {
        return userAddressRepository.findByUserAndAddressType(user, addressType);
    }

    public void deleteAddress(UserAddress address) {
        userAddressRepository.delete(address);
    }
}
