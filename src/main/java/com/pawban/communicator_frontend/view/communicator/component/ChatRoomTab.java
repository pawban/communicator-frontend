package com.pawban.communicator_frontend.view.communicator.component;

import com.pawban.communicator_frontend.domain.ChatRoom;
import com.vaadin.flow.component.tabs.Tab;

import java.util.UUID;

public class ChatRoomTab extends Tab {

    private final ChatRoom chatRoom;

    public ChatRoomTab(final ChatRoom chatRoom) {
        super(chatRoom.getName());
        this.chatRoom = chatRoom;
    }

    public UUID getChatRoomId() {
        return chatRoom.getId();
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

}
