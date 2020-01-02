package com.pawban.communicator_frontend.view.communicator.component;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class DeleteChatRoomIcon extends Icon {

    public DeleteChatRoomIcon() {
        super(VaadinIcon.CLOSE_CIRCLE_O);
        setColor("var(--lumo-error-color)");
        setSize("18px");
        getStyle().set("cursor", "pointer").set("margin-right", "1em");
        getElement().setAttribute("title", "Delete current chat room");
    }

}
