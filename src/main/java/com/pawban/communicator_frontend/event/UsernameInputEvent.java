package com.pawban.communicator_frontend.event;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DebounceSettings;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.DebouncePhase;

@DomEvent(
        value = "input",
        debounce = @DebounceSettings(
                timeout = 250,
                phases = DebouncePhase.TRAILING
        )
)
public class UsernameInputEvent extends ComponentEvent<TextField> {

    public UsernameInputEvent(TextField source, boolean fromClient) {
        super(source, fromClient);
    }

}
