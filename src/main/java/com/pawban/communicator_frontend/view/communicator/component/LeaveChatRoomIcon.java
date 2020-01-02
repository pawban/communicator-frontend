package com.pawban.communicator_frontend.view.communicator.component;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class LeaveChatRoomIcon extends Icon {

    public LeaveChatRoomIcon() {
        super(VaadinIcon.CLOSE);
        getStyle().set("cursor", "pointer").set("margin-right", "1em");
        setSize("12px");
        getElement().setAttribute("title", "Leave current chat room");
    }

}
