package com.pawban.communicator_frontend.session;

import com.pawban.communicator_frontend.domain.ChatRoom;
import com.pawban.communicator_frontend.domain.User;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@VaadinSessionScope
@NoArgsConstructor
@Getter
@Setter
public class CommunicatorSession {

    private UUID sessionId;
    private User user;
    private UUID currentChatRoomId;
    private Set<ChatRoom> chatRooms = new HashSet<>();

    public void setSession(final SessionData session) {
        this.sessionId = session.getSessionId();
        this.user = session.getUser();
    }

    public boolean isCurrentUserMemberOf(final UUID chatRoomId) {
        return chatRooms.stream()
                .anyMatch(chatRoom -> chatRoom.getId().equals(chatRoomId));
    }

    public boolean isCurrentUserOwnerOf(final ChatRoom chatRoom) {
        return chatRoom.getOwner().getId().equals(user.getId());
    }

    public Optional<ChatRoom> getChatRoom(final UUID chatRoomId) {
        return chatRooms.stream()
                .filter(room -> room.getId().equals(chatRoomId))
                .findFirst();
    }

}
