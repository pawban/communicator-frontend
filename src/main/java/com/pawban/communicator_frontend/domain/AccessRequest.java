package com.pawban.communicator_frontend.domain;

import com.pawban.communicator_frontend.dto.AccessRequestDto;
import com.pawban.communicator_frontend.type.AccessRequestStatus;
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
public class AccessRequest {

    private UUID id;
    private User sender;
    private ChatRoom chatRoom;
    private String request;
    private AccessRequestStatus status;

    public AccessRequest(final AccessRequestDto accessRequestDto) {
        this(
                accessRequestDto.getId(),
                new User(accessRequestDto.getSender()),
                new ChatRoom(accessRequestDto.getChatRoom()),
                accessRequestDto.getRequest(),
                accessRequestDto.getStatus()
        );
    }

}
