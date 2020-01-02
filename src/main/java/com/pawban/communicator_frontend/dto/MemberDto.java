package com.pawban.communicator_frontend.dto;

import com.pawban.communicator_frontend.type.MembershipRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberDto {

    private UserDto user;
    private MembershipRole role;

}
