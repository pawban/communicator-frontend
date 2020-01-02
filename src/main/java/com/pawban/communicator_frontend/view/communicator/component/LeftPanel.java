package com.pawban.communicator_frontend.view.communicator.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LeftPanel extends VerticalLayout {

    public LeftPanel(Component... components) {
        setPadding(false);
        add(components);
    }

}
