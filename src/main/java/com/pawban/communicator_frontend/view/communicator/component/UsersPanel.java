package com.pawban.communicator_frontend.view.communicator.component;

import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.Lumo;

public class UsersPanel extends VerticalLayout {

    public UsersPanel() {
        getThemeList().add(Lumo.DARK);
        add(new H4("Users"));
    }

}
