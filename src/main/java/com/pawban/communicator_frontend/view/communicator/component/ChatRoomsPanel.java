package com.pawban.communicator_frontend.view.communicator.component;

import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.Lumo;

public class ChatRoomsPanel extends VerticalLayout {

    public ChatRoomsPanel(final Icon createNewChatRoomIcon) {
        getThemeList().add(Lumo.DARK);
        H4 title = new H4("Chat rooms");
        createNewChatRoomIcon.getStyle().set("margin-top", "1.25em");
        HorizontalLayout header = new HorizontalLayout(title, createNewChatRoomIcon);
        header.setAlignItems(Alignment.CENTER);
        header.setWidthFull();
        header.getStyle().set("margin-top", "0");
        header.expand(title);
        add(header);
    }

}
