package com.pawban.communicator_frontend.domain;

import com.pawban.communicator_frontend.dto.MemberDto;
import com.pawban.communicator_frontend.type.MembershipRole;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Member {

    private User user;
    private MembershipRole role;

    public Member(final MemberDto memberDto) {
        this(new User(memberDto.getUser()), memberDto.getRole());
    }

}
