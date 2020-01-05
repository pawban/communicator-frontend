package com.pawban.communicator_frontend.domain;

import com.pawban.communicator_frontend.dto.ChatRoomDto;
import com.pawban.communicator_frontend.type.ChatRoomStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatRoom {

    private UUID id;
    private String name;
    private ChatRoomStatus status;
    private User owner;

    public ChatRoom(final ChatRoomDto chatRoomDto) {
        this(chatRoomDto.getId(), chatRoomDto.getName(), chatRoomDto.getStatus(), new User(chatRoomDto.getOwner()));
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
        ChatRoom chatRoom = (ChatRoom) o;
        return id.equals(chatRoom.id);
    }

}
