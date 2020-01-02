package com.pawban.communicator_frontend.dto;

import com.pawban.communicator_frontend.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private UUID id;
    private String username;
    private CountryDto country;
    private Boolean visible;

    public UserDto(final User user) {
        this(
                user.getId(),
                user.getUsername(),
                new CountryDto(user.getCountry()),
                user.getVisible()
        );
    }

}
