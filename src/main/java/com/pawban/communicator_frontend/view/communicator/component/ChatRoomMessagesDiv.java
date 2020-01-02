package com.pawban.communicator_frontend.view.communicator.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;

import java.util.Collection;
import java.util.UUID;

public class ChatRoomMessagesDiv extends Div {

    private final UUID chatRoomId;

    public ChatRoomMessagesDiv(final UUID chatRoomId) {
        this.chatRoomId = chatRoomId;
        setVisible(false);
        setHeightFull();
        getStyle().set("margin", "0");
    }

    public UUID getChatRoomId() {
        return chatRoomId;
    }

    public void add(Collection<Component> components) {
        components.forEach(this::add);
    }

}
