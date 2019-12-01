package com.pawban.communicator_frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SessionDto {

    private UUID sessionId;
    private UserDto userDto;

}

