package com.pawban.communicator_frontend.view.communicator.component;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class DeleteUserIcon extends Icon {

    public DeleteUserIcon() {
        super(VaadinIcon.CLOSE);
        setSize("12px");
        getStyle().set("cursor", "pointer");
        getElement().setAttribute("title", "Delete current user and leave communicator");
    }

}
