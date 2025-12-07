package com.Fuad.BankApplicationSimulation.DTO.CustomerDTO.RequestDTO;


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

    @NotBlank
    @Size(max = 50)
    //TODO: Ispravit pod AZ nomer
    @Pattern(regexp = "\\+?[0-9]{10,15}", message = "Invalid phone number")
    private String phoneNumber;

    @NotBlank
    @Size(max = 50)
    private String address;

}
