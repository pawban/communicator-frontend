package com.pawban.communicator_frontend.view.communicator.component;

import com.pawban.communicator_frontend.domain.ChatRoom;
import com.vaadin.flow.component.tabs.Tabs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ChatRoomsTabs extends Tabs {

    private final Map<UUID, ChatRoomTab> tabs = new HashMap<>();

    public ChatRoomsTabs() {
        super(false);
        setWidthFull();
    }

    public void add(final ChatRoomTab tab) {
        tabs.put(tab.getChatRoomId(), tab);
        super.add(tab);
    }

    public ChatRoomTab removeTab(final UUID chatRoomId) {
        ChatRoomTab tab = tabs.remove(chatRoomId);
        remove(tab);
        return tab;
    }

    @Override
    public void removeAll() {
        tabs.clear();
        super.removeAll();
    }

    public ChatRoomTab getSelectedTab() {
        return (ChatRoomTab) super.getSelectedTab();
    }

    public Set<ChatRoom> getDisplayedChatRooms() {
        return tabs.values().stream()
                .map(ChatRoomTab::getChatRoom)
                .collect(Collectors.toSet());
    }

    public boolean isNotDisplayed(final ChatRoom chatRoom) {
        return !tabs.containsKey(chatRoom.getId());
    }

}
