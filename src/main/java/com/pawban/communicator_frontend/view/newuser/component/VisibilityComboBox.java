package com.pawban.communicator_frontend.view.newuser.component;

import com.vaadin.flow.component.combobox.ComboBox;

public class VisibilityComboBox extends ComboBox<Boolean> {

    public VisibilityComboBox(String label, String placeholder) {
        super(label);
        setItems(Boolean.TRUE, Boolean.FALSE);
        setItemLabelGenerator(item -> item ? "visible" : "hidden");
        setPlaceholder(placeholder);
        setRequired(true);
        setWidthFull();
    }

}
