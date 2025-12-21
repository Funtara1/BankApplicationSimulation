package com.fuad.bank.api.dto.CustomerDTO.RequestDTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class CreateCustomerRequest {

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String surname;

    @NotBlank
    @Size(min = 7, max = 7)
    private String fin;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\+994(50|51|55|10|99|70|77|60)[1-9][0-9]{6}$",
            message = "Phone number must be in AZ format: +994XXXXXXXXX"
    )
    private String phoneNumber;

    @NotBlank
    @Size(max = 50)
    private String address;

}
