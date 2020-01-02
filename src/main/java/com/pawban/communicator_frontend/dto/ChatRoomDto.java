package com.pawban.communicator_frontend.dto;

import com.pawban.communicator_frontend.domain.ChatRoom;
import com.pawban.communicator_frontend.type.ChatRoomStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatRoomDto {

    private UUID id;
    private String name;
    private ChatRoomStatus status;
    private UserDto owner;

    public ChatRoomDto(final ChatRoom chatRoom) {
        this(chatRoom.getId(), chatRoom.getName(), chatRoom.getStatus(), new UserDto(chatRoom.getOwner()));
    }

}
