package com.pawban.communicator_frontend.domain;

import com.pawban.communicator_frontend.dto.UserStatusDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserStatus {

    private String status;
    private Boolean visible;

    public UserStatus(final UserStatusDto userStatusDto) {
        this(
                userStatusDto.getStatus(),
                userStatusDto.getVisible()
        );
    }

}
