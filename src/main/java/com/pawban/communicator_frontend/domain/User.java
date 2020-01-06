package com.pawban.communicator_frontend.domain;

import com.pawban.communicator_frontend.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
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
    private Boolean visible;

    public User(final UserDto userDto) {
        this(userDto.getId(), userDto.getUsername(), new Country(userDto.getCountry()), userDto.getVisible());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id.equals(user.id);
    }

}
