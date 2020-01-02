package com.pawban.communicator_frontend.view.newuser.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

public class EnterButton extends Button {

    public EnterButton(String label) {
        super(label);
        addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        setWidthFull();
    }

}
