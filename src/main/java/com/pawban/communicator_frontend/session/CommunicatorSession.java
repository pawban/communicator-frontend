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

    private final Set<ChatRoom> chatRooms = new HashSet<>();
    private UUID sessionId;
    private User currentUser;
    private UUID currentChatRoomId;

    public void setSession(final SessionData session) {
        this.sessionId = session.getSessionId();
        this.currentUser = session.getUser();
    }

    public boolean isCurrentUserMemberOf(final ChatRoom chatRoom) {
        return chatRooms.stream()
                .anyMatch(cr -> cr.equals(chatRoom));
    }

    public boolean isCurrentUserOwnerOf(final ChatRoom chatRoom) {
        return chatRoom.getOwner().equals(currentUser);
    }

    public Optional<ChatRoom> getChatRoom(final UUID chatRoomId) {
        return chatRooms.stream()
                .filter(cr -> cr.getId().equals(chatRoomId))
                .findFirst();
    }

}
