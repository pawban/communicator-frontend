package com.pawban.communicator_frontend.view.communicator.dialog;

import com.pawban.communicator_frontend.domain.AccessRequest;
import com.pawban.communicator_frontend.type.AccessRequestStatus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ProcessedAccessRequestNotification extends Notification {

    public ProcessedAccessRequestNotification(final AccessRequest accessRequest) {
        Label message = new Label("Your access request to join chat room '" + accessRequest.getChatRoom().getName() +
                "' has been " + accessRequest.getStatus().getValue() + ".");
        message.getStyle().set("margin-right", "0.5rem");

        Button button = new Button("OK");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(buttonClickEvent -> close());

        HorizontalLayout layout = new HorizontalLayout(message, button);
        layout.setMargin(false);
        layout.setPadding(false);

        NotificationVariant variant = accessRequest.getStatus().equals(AccessRequestStatus.ACCEPTED) ?
                NotificationVariant.LUMO_SUCCESS :
                NotificationVariant.LUMO_ERROR;
        setPosition(Position.MIDDLE);
        addThemeVariants(variant);
        add(layout);
        open();
    }

}
