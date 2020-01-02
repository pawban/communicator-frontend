package com.pawban.communicator_frontend.view.communicator.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.theme.lumo.Lumo;

public class SendMessageButton extends Button {

    public SendMessageButton() {
        setText("Send");
        setThemeName(Lumo.LIGHT);
        disable();
    }

    public void disable() {
        setEnabled(false);
    }

    public void enable() {
        setEnabled(true);
    }

}
