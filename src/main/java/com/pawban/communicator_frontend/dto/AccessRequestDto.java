package com.pawban.communicator_frontend.dto;

import com.pawban.communicator_frontend.domain.AccessRequest;
import com.pawban.communicator_frontend.type.AccessRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccessRequestDto {

    private UUID id;
    private UserDto sender;
    private ChatRoomDto chatRoom;
    private String request;
    private AccessRequestStatus status;

    public AccessRequestDto(final AccessRequest accessRequest) {
        this(
                accessRequest.getId(),
                new UserDto(accessRequest.getSender()),
                new ChatRoomDto(accessRequest.getChatRoom()),
                accessRequest.getRequest(),
                accessRequest.getStatus()
        );
    }

}
