package com.pawban.communicator_frontend.view.communicator.component;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class CreateNewChatRoomIcon extends Icon {

    public CreateNewChatRoomIcon() {
        super(VaadinIcon.PLUS);
        setSize("20px");
        getStyle().set("cursor", "pointer");
        getElement().setAttribute("title", "Create new chat room");
    }

}
