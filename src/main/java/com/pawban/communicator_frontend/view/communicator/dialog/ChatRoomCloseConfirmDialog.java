package com.pawban.communicator_frontend.view.communicator.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ChatRoomCloseConfirmDialog extends Dialog {

    public ChatRoomCloseConfirmDialog(final Runnable confirmAction) {
        H3 title = new H3("Close chat room confirmation");
        title.getStyle().set("margin", "auto");

        Label textLabel = new Label("Are you sure you want to leave this chat room? You'll loose access to it. " +
                "New access request is required to rejoin this chat room.");
        textLabel.setWidthFull();

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(buttonClickEvent -> this.close());

        Button confirmButton = new Button("Leave chat room");
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
        open();
    }

}
