package com.pawban.communicator_frontend.domain;

import com.pawban.communicator_frontend.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User {

    private UUID id;
    private String username;
    private Country country;
    private UserStatus status;

    public User(final UserDto userDto) {
        this(
                userDto.getId(),
                userDto.getUsername(),
                new Country(userDto.getCountry()),
                new UserStatus(userDto.getStatus())
        );
    }

}
