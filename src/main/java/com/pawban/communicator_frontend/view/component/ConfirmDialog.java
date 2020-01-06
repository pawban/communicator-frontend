package com.pawban.communicator_frontend.view.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ConfirmDialog extends Dialog {

    public ConfirmDialog(final String titleText,
                         final String messageText,
                         final String confirmButtonText,
                         final Runnable action) {
        H3 title = new H3(titleText);
        title.getStyle().set("margin", "auto");

        Label textLabel = new Label(messageText);
        textLabel.setWidthFull();

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(buttonClickEvent -> close());
        cancelButton.focus();

        Button confirmButton = new Button(confirmButtonText);
        confirmButton.getStyle().set("margin-left", "auto");
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(buttonClickEvent -> {
            action.run();
            close();
        });

        HorizontalLayout buttons = new HorizontalLayout(cancelButton, confirmButton);
        buttons.setWidthFull();

        VerticalLayout mainLayout = new VerticalLayout(title, textLabel, buttons);

        add(mainLayout);
        setWidth("500px");
        open();
    }

}
