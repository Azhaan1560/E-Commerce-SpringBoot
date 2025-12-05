package com.ESD.ecomm.dto.address;

import com.ESD.ecomm.enums.AddressType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressUpdateDTO {

    @NotNull(message = "Address ID is required")
    private Long id;

    private AddressType addressType;

    @Size(max = 100)
    private String fullName;

    @Size(max = 20)
    private String phoneNumber;

    @Size(max = 255)
    private String addressLine1;

    @Size(max = 255)
    private String addressLine2;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String state;

    @Size(max = 100)
    private String country;

    @Size(max = 20)
    private String zipCode;

    private Boolean isDefault;
}
