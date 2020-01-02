package com.pawban.communicator_frontend.view.communicator.component;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.Lumo;

public class EnterMessagePanel extends HorizontalLayout {

    public EnterMessagePanel() {
        setHeight("100px");
        setAlignItems(Alignment.CENTER);
        setWidthFull();
        getThemeList().add(Lumo.DARK);
        setPadding(true);
    }

}
