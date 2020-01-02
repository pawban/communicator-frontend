package com.pawban.communicator_frontend.view.communicator.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatRoomMessagesPanel extends VerticalLayout {

    private final Map<UUID, Component> chatRooms = new HashMap<>();
    private final VerticalLayout content = new VerticalLayout();

    public ChatRoomMessagesPanel() {
        preparePanel();
        getStyle().set("border", "1px solid grey");
    }

    private void preparePanel() {
        setSizeFull();
        setMargin(false);
        setPadding(false);
        setSpacing(false);
        getStyle().set("overflow", "auto");

        content.setSizeFull();
        content.getStyle()
                .set("padding", "var(--lumo-space-xs)")
                .set("display", "block");
        super.add(content);
    }

    @Override
    public void remove(Component... components) {
        content.remove(components);
    }

    @Override
    public void removeAll() {
        content.removeAll();
    }

    public void add(ChatRoomMessagesDiv... components) {
        content.add(components);
        for (ChatRoomMessagesDiv component : components) {
            chatRooms.put(component.getChatRoomId(), component);
        }
    }

    public void remove(final UUID chatRoomId) {
        content.remove(chatRooms.remove(chatRoomId));
    }

}
