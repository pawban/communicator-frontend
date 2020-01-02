package com.pawban.communicator_frontend.view.newuser.component;

import com.pawban.communicator_frontend.domain.Country;
import com.vaadin.flow.component.combobox.ComboBox;

public class CountriesComboBox extends ComboBox<Country> {

    public CountriesComboBox(String label, String placeholder) {
        super(label);
        setItemLabelGenerator(Country::getName);
        setPlaceholder(placeholder);
        setRequired(true);
        setAllowCustomValue(false);
        setWidthFull();

    }

}
