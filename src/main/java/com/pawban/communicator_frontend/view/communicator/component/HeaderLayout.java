package com.pawban.communicator_frontend.view.communicator.component;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.Lumo;

public class HeaderLayout extends HorizontalLayout {

    public HeaderLayout() {
        getThemeList().add(Lumo.DARK);
        setAlignItems(Alignment.CENTER);
        setWidthFull();
        setPadding(true);
    }

}
