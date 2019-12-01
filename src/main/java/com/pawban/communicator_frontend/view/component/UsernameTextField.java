package com.pawban.communicator_frontend.view.component;

import com.pawban.communicator_frontend.event.UsernameInputEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

public class UsernameTextField extends TextField {

    public UsernameTextField(String text) {
        super(text);
    }

    public Registration addMyInputListener(ComponentEventListener<UsernameInputEvent> listener) {
        return addListener(UsernameInputEvent.class, listener);
    }

}
