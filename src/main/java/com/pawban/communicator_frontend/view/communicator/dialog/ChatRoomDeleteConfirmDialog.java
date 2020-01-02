package com.pawban.communicator_frontend.view.communicator.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ChatRoomDeleteConfirmDialog extends Dialog {

    public ChatRoomDeleteConfirmDialog(final Runnable confirmAction) {
        H3 title = new H3("Chat room delete confirmation");
        title.getStyle().set("margin", "auto");

        Label textLabel = new Label("Are you sure you want to delete this chat room? This operation is " +
                "irreversible!");
        textLabel.setWidthFull();

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(buttonClickEvent -> this.close());

        Button confirmButton = new Button("Delete chat room");
        confirmButton.getStyle().set("margin-left", "auto");
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(buttonClickEvent -> {
            confirmAction.run();
            this.close();
        });

        HorizontalLayout buttons = new HorizontalLayout(cancelButton, confirmButton);
        buttons.setWidthFull();

        VerticalLayout mainLayout = new VerticalLayout(title, textLabel, buttons);

        add(mainLayout);
        setWidth("500px");
    }

}
