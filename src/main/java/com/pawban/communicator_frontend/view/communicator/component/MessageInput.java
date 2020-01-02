package com.pawban.communicator_frontend.view.communicator.component;

import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;

public class MessageInput extends TextArea {

    public MessageInput() {
        setPlaceholder("Enter new message...");
        setHeightFull();
        disable();
        setValueChangeMode(ValueChangeMode.EAGER);
    }

    public void disable() {
        setEnabled(false);
    }

    public void enable() {
        setEnabled(true);
    }

}
