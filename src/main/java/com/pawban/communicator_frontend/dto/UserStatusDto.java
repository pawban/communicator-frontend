package com.pawban.communicator_frontend.dto;

import com.pawban.communicator_frontend.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserStatusDto {

    private String status;
    private Boolean visible;

    public UserStatusDto(final UserStatus userStatus) {
        this(
                userStatus.getStatus(),
                userStatus.getVisible()
        );
    }

}
