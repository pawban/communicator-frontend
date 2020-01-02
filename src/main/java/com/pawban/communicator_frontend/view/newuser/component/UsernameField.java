package com.pawban.communicator_frontend.view.newuser.component;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class UsernameField extends TextField {

    public UsernameField(String label, String placeholder) {
        super(label);
        setWidthFull();
        setPlaceholder(placeholder);
        setValueChangeMode(ValueChangeMode.LAZY);
        focus();
    }

}
