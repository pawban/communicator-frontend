package com.pawban.communicator_frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UsernameValidationDto {

    private String username;
    private Boolean available;

}
